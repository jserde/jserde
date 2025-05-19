/*
 * Copyright 2025 JSerde
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException
import java.io.PrintStream
import java.io.Reader
import java.time.Instant
import javax.annotation.processing.Generated
import org.jboss.forge.roaster.Roaster
import org.jboss.forge.roaster.model.source.JavaClassSource
import org.jboss.forge.roaster.model.source.JavaInterfaceSource
import org.jboss.forge.roaster.model.source.JavaSource
import org.jboss.forge.roaster.model.source.MethodSource

private fun normalizeMembersAccess(source: JavaClassSource) {
    source.members.forEach {
        if (it.isPublic || it.isProtected) {
            it.setPackagePrivate()
        }
    }
}

private fun overrideCloseMethod(source: JavaClassSource): MethodSource<JavaClassSource> =
    source.addMethod()
        .setPublic()
        .setReturnType("void")
        .setName("close")
        .addThrows(IOException::class.java)
        .also { it.addAnnotation(Override::class.java) }

private fun <T: JavaSource<T>> processSource(source: T) {
    // Add @Generated annotation
    source.addAnnotation(Generated::class.java)
        .setStringValue("JavaCC")
        .setStringValue("date", Instant.now().toString())
    // Add @NullUnmarked annotation
    source.addAnnotation("org.jspecify.annotations.NullUnmarked")
    // Remove @SuppressWarnings annotation if any
    source.removeAnnotation(SuppressWarnings::class.java)
}

private fun processParser(source: JavaClassSource) {
    processSource(source)
    // Add @SuppressWarnings annotation
    source.addAnnotation(SuppressWarnings::class.java)
        .setStringArrayValue(arrayOf(
            // JavaCC doesn't support Java 11+ features
            "PatternMatchingInstanceof",
            "StatementSwitchToExpressionSwitch",
            // JavaCC generates ugly code
            "AssignmentExpression",
            "EmptyCatch",
            "StaticAssignmentOfThrowable",
            "UnnecessaryParentheses",
            "UnusedMethod",
            "UnusedVariable",
        ))
    // Make the class extend AbstractTokenManager
    val superclassSource = Roaster.create(JavaClassSource::class.java)
        .setPackage("jserde.javacc")
        .setName("AbstractParser")
    source.extendSuperType(superclassSource)
    // Make class final
    source.isFinal = true
    // Make public members package-private
    normalizeMembersAccess(source)
    // Override close() method
    overrideCloseMethod(source).body = "token_source.close();"
    // Remove unused fields
    source.removeField("trace_enabled")
    // Remove unused methods
    source.methods.forEach {
        if (it.name == "ReInit") {
            source.removeMethod(it)
        }
    }
    source.removeMethod("trace_enabled")
    source.removeMethod("enable_tracing")
    source.removeMethod("disable_tracing")
    // Simplify body of constructor
    source.getConstructor("CharStream")?.body = "this(new ${source.name}TokenManager(stream));"
    /* NOTE:
     * The generateParseException method references the token manager class even when a custom token manager is used.
     * We therefore replace its return statement with a better one.
     */
    source.getMethod("generateParseException")?.let {
        it.body = it.body?.replace(
            Regex("return [^;]+;"),
            "return new ParseException(token, exptokseq);"
        )
    }
}

private fun processTokenManager(source: JavaClassSource) {
    processSource(source)
    // Add @SuppressWarnings annotation
    source.addAnnotation(SuppressWarnings::class.java)
        .setStringArrayValue(arrayOf(
            // JavaCC doesn't support Java 11+ features
            "PatternMatchingInstanceof",
            "StatementSwitchToExpressionSwitch",
            // JavaCC generates ugly code
            "AssignmentExpression",
            "EmptyCatch",
            "UnnecessaryParentheses",
            "UnusedMethod",
            "UnusedVariable",
        ))
    // Make the class extend AbstractTokenManager
    val superclassSource = Roaster.create(JavaClassSource::class.java)
        .setPackage("jserde.javacc")
        .setName("AbstractTokenManager")
    source.extendSuperType(superclassSource)
    // Make class final
    source.isFinal = true
    // Make some fields final
    source.getField("input_stream")?.isFinal = true
    // Make public members package-private
    normalizeMembersAccess(source)
    // Override close() method
    overrideCloseMethod(source).body = "input_stream.close();"
    // Remove unused constructor (additionally, this constructor is poorly written and invokes ReInit, so it should be removed anyway)
    source.constructors.single {
        it.parameters.size == 2
            && it.parameters[0].name == "stream"
            && it.parameters[1].name == "lexState"
    }
        .let { source.removeMethod(it) }
    // Remove unused methods
    // NOTE: The ReInitRounds method is used internally and must not be removed
    source.methods.forEach {
        if (it.name == "ReInit") {
            source.removeMethod(it)
        }
    }
    // Remove debug-related members
    source.removeField("debugStream")
    source.removeMethod("setDebugStream", PrintStream::class.java)
    // Simplify body of method SwitchTo by removing validation
    source.getMethod("SwitchTo", "int")?.body = "curLexState = lexState;"
}

private fun processConstants(source: JavaInterfaceSource) {
    processSource(source)
    // Add @SuppressWarnings annotation
    source.addAnnotation(SuppressWarnings::class.java)
        .setStringArrayValue(arrayOf(
            // For the tokenImage static field
            "MutablePublicArray",
        ))
}

/**
 * [java.io.FilterReader] that processes the parser class source generated by JavaCC.
 */
class ParserFilter(reader: Reader) : RoasterFilter<JavaClassSource>(reader, JavaClassSource::class.java, ::processParser)

/**
 * [java.io.FilterReader] that processes the token manager class source generated by JavaCC.
 */
class TokenManagerFilter(reader: Reader) : RoasterFilter<JavaClassSource>(reader, JavaClassSource::class.java, ::processTokenManager)

/**
 * [java.io.FilterReader] that processes the constants interface source generated by JavaCC.
 */
class ConstantsFilter(reader: Reader) : RoasterFilter<JavaInterfaceSource>(reader, JavaInterfaceSource::class.java, ::processConstants)
