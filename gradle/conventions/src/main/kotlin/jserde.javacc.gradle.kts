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

import smartefact.gradle.javacc.JavaccTask

plugins {
    id("jserde.java-library")
    id("smartefact.javacc")
}

dependencies {
    implementation(project(":jserde-javacc"))
}

javacc {
    javaccVersion.set("7.0.13")
    // NOTE: These are recommended options
    // NOTE: Due to a bug in JavaCC 7.0.13, this option must not be set here
    // javaTemplateType.set("modern")
    generateBoilerplate.set(false)
    generateChainedException.set(true)
    generateStringBuilder.set(true)
    staticMembers.set(false)
    supportClassVisibilityPublic.set(false)
}

val javacc by tasks.registering(JavaccTask::class) {
    description = "Generates the sources with JavaCC."
    group = BasePlugin.BUILD_GROUP
    inputFile(
        fileTree(layout.projectDirectory.dir("src/main/javacc")) {
            include("*.jj")
        }
            .single()
    )
    sourceSet.set(SourceSet.MAIN_SOURCE_SET_NAME)
    doLast {
        // Process the Java sources generated by JavaCC
        // NOTE: This assumes the JavaCC grammar has the same base name as the generated parser class
        val baseName = inputFile.get().asFile.nameWithoutExtension
        // NOTE: Since we cannot copy/sync a directory into itself, we use an intermediate temporary directory
        val tmpDir = layout.buildDirectory.dir("tmp/$name")
        sync {
            from(outputDirectory)
            into(tmpDir)
        }
        sync {
            // Parser
            from(tmpDir) {
                include("**/$baseName.java")
                filter(ParserFilter::class)
            }
            // Token manager
            from(tmpDir) {
                include("**/${baseName}TokenManager.java")
                filter(TokenManagerFilter::class)
            }
            // Constants
            from(tmpDir) {
                include("**/${baseName}Constants.java")
                filter(ConstantsFilter::class)
            }
            /* NOTE:
             * When JavaCC is configured to use a custom token manager and not to generate boilerplate,
             * it still generates a TokenManager interface.
             * That generated interface is ugly, so it is excluded here in favor of the one provided by the project.
             */
            into(outputDirectory)
        }
    }
}
