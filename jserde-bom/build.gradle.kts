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

plugins {
    id("jserde.base")
    id("java-platform")
    id("jserde.maven-publish")
}

description = "JSerde BOM."

dependencies {
    constraints {
        rootProject.subprojects
            .filter { subproject ->
                // Because the subprojects may not be configured yet, we cannot rely on its plugins or extensions to determine if it should be included in the BOM
                // We do this ugly trick instead
                // TODO #build: Find a cleaner way to determine subprojects to include in the BOM
                val buildFile = subproject.projectDir.resolve("build.gradle.kts")
                buildFile.readText(Charsets.UTF_8).contains("id(\"jserde.java-library\")")
            }
            .forEach { api(it) }
    }
}

publishing {
    publications {
        val main by registering(MavenPublication::class) {
            from(components["javaPlatform"])
        }
    }
}
