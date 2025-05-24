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

import java.io.FilterReader
import java.io.Reader
import org.jboss.forge.roaster.Roaster
import org.jboss.forge.roaster.model.source.AnnotationTargetSource
import org.jboss.forge.roaster.model.source.FieldHolderSource
import org.jboss.forge.roaster.model.source.JavaClassSource
import org.jboss.forge.roaster.model.source.JavaSource
import org.jboss.forge.roaster.model.source.MethodHolderSource
import org.jboss.forge.roaster.model.source.MethodSource

val JavaClassSource.constructors: List<MethodSource<JavaClassSource>>
    get() = methods.filter { it.isConstructor }

fun JavaClassSource.getConstructor(): MethodSource<JavaClassSource>? =
    getMethod(name)

fun JavaClassSource.getConstructor(vararg paramTypes: String): MethodSource<JavaClassSource>? =
    getMethod(name, *paramTypes)

fun JavaClassSource.getConstructor(vararg paramTypes: Class<*>): MethodSource<JavaClassSource>? =
    getMethod(name, *paramTypes)

fun <O: JavaSource<O>> FieldHolderSource<O>.removeField(name: String): Boolean =
    getField(name)?.let { removeField(it) } != null

fun JavaClassSource.removeConstructor(): Boolean =
    removeMethod(name)

fun JavaClassSource.removeConstructor(vararg paramTypes: String): Boolean =
    removeMethod(name, *paramTypes)

fun JavaClassSource.removeConstructor(vararg paramTypes: Class<*>): Boolean =
    removeMethod(name, *paramTypes)

fun <O: JavaSource<O>> MethodHolderSource<O>.removeMethod(name: String): Boolean =
    getMethod(name)?.let { removeMethod(it) } != null

fun <O: JavaSource<O>> MethodHolderSource<O>.removeMethod(name: String, vararg paramTypes: String): Boolean =
    getMethod(name, *paramTypes)?.let { removeMethod(it) } != null

fun <O: JavaSource<O>> MethodHolderSource<O>.removeMethod(name: String, vararg paramTypes: Class<*>): Boolean =
    getMethod(name, *paramTypes)?.let { removeMethod(it) } != null

fun <O: JavaSource<O>, T> AnnotationTargetSource<O, T>.removeAnnotation(type: Class<out Annotation>): Boolean =
    getAnnotation(type)?.let { removeAnnotation(it) } != null

/**
 * [FilterReader] that processes a source file with Roaster.
 */
abstract class RoasterFilter<T: JavaSource<T>>(
    reader: Reader,
    type: Class<out T>,
    processor: (T) -> Unit
) : FilterReader(processed(reader, type, processor)) {
    companion object {
        /**
         * Reads the whole text from [reader], parses it with Roaster, processes the resulting source, and returns a [Reader] on that processed source.
         */
        private inline fun <T: JavaSource<T>> processed(reader: Reader, type: Class<out T>, processor: (T) -> Unit): Reader {
            val source = Roaster.parse(type, reader.readText())
            processor(source)
            return source.toString().reader()
        }
    }
}
