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

import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
    id("jserde.asciidoctor")
}

description = "JSerde documentation."

// NOTE: As of Gradle 8.13, there is no way to make the Javadoc task work with multiple modules, so we do it manually
// TODO #build: Generate the aggregate Javadoc in a more standard way when it's possible
val aggregateJavadoc by tasks.registering(Exec::class) {
    description = "Generate the aggregated Javadoc documentation."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    val title = "JSerde ${project.version} API"
    // We cannot use the pattern syntax for the module source paths, so we need to extract the module names
    val moduleNameRegex = Regex("module\\s+([a-zA-Z0-9._]+)")
    val moduleSourcePaths = mutableListOf<Pair<String, File>>()
    val modulePath = project.files()
    val sourceFiles = mutableListOf<File>()
    // Collect subprojects...
    rootProject.subprojects
        // ... that use the Java plugin...
        .mapNotNull { subproject -> subproject.extensions.findByType(JavaPluginExtension::class) }
        // ... and get their main source set...
        .map { javaExtension -> javaExtension.sourceSets[SourceSet.MAIN_SOURCE_SET_NAME] }
        .forEach { sourceSet ->
            // ... and collect their Java source directories (typically only one, i.e. `src/main/java`)...
            sourceSet.java.sourceDirectories.forEach { javaSourceDir ->
                // ... and read the contents of their `module-info.java` file...
                javaSourceDir.resolve("module-info.java").takeIf { it.isFile }?.readText()?.let { moduleInfo ->
                    // ... and extract the module name from it...
                    moduleNameRegex.find(moduleInfo)?.groupValues?.get(1)?.let { moduleName ->
                        // ... and create the corresponding module source path
                        moduleSourcePaths += moduleName to javaSourceDir
                    }
                }
            }
            // ... and add their classpath to the module path...
            modulePath.from(sourceSet.compileClasspath)
            // ... and add their Java sources to the source files
            sourceFiles += sourceSet.java
        }
    // Package groups
    val groups = mapOf(
        "Core" to setOf("jserde.core", "jserde.core.de", "jserde.core.ser"),
        "Service" to setOf("jserde.service.de", "jserde.service.ser"),
        "JSON" to setOf("jserde.json", "jserde.json.de", "jserde.json.model", "jserde.json.ser"),
        "JSON5" to setOf("jserde.json5", "jserde.json5.de", "jserde.json5.ser"),
        "TOML" to setOf("jserde.toml", "jserde.toml.de", "jserde.toml.ser"),
    )
    // Collect subprojects...
    val links = rootProject.subprojects.flatMap { subproject ->
        // ... and get their Javadoc tasks (typically only one, i.e. `javadoc`)...
        subproject.tasks.withType(Javadoc::class.java).flatMap { javadocTask ->
            // ... and collect their links
            javadocTask.standardOptions.links.orEmpty()
        }
    }
        .toSet()
    val destDir = layout.buildDirectory.dir("docs/javadoc").get().asFile
    // Write options into a temporary file
    val optionsFile = layout.buildDirectory.file("tmp/$name/javadoc.options").get().asFile
    optionsFile.parentFile.mkdirs()
    optionsFile.printWriter().use { writer ->
        moduleSourcePaths.forEach { (name, path) ->
            writer.println("--module-source-path '$name=${path.path}'")
        }
        writer.println("--module-path '${modulePath.asPath}'")
        writer.println("-encoding UTF-8")
        writer.println("-d '${destDir.path}'")
        writer.println("-docencoding UTF-8")
        writer.println("-locale en_US")
        writer.println("-docfilessubdirs")
        writer.println("-doctitle '$title'")
        writer.println("-windowtitle '$title'")
        writer.println("-bottom 'Copyright &copy; 2025 JSerde'")
        writer.println("-protected")
        writer.println("--show-members protected")
        writer.println("--show-module-contents api")
        writer.println("--show-packages exported")
        writer.println("--show-types protected")
        writer.println("-version")
        writer.println("-author")
        writer.println("-notimestamp")
        groups.forEach { (name, packages) ->
            writer.println("-group '$name' '${packages.joinToString(":")}'")
        }
        links.forEach { link ->
            writer.println("-link '$link'")
        }
        writer.println("-quiet")
        writer.println("-Xdoclint:html,reference,syntax")
        sourceFiles.forEach { sourceFile ->
            writer.println(sourceFile.path)
        }
    }
    // Use the same Javadoc tool as the jserde-core project
    val javadocTool = project(":jserde-core").tasks.getByName<Javadoc>("javadoc").javadocTool.get()
    // Execute Javadoc tool with options file
    executable(javadocTool.executablePath)
    args("@${optionsFile.path}")
}

val userGuide by tasks.registering(AsciidoctorTask::class) {
    description = "Generates the JSerde User Guide."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    sourceDir(layout.projectDirectory.dir("src/docs"))
    sources("jserde-user-guide.adoc")
    setOutputDir(layout.buildDirectory.dir("docs"))
}

tasks.assemble {
    dependsOn(
        aggregateJavadoc,
        userGuide,
    )
}
