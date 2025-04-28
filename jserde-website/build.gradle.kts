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
}

description = "JSerde website."

// Pico CSS theme
// https://picocss.com/docs/version-picker
val picocssTheme = "azure"

val jserdeDocsProject = project(":jserde-docs")

val website by tasks.registering(Sync::class) {
    description = "Generates the JSerde website."
    dependsOn(jserdeDocsProject.tasks["assemble"])
    from(layout.projectDirectory.dir("src/website")) {
        expand(
            "projectVersion" to project.version.toString(),
            "picocssVersion" to "2.1.0",
            // NOTE: "azure" being the default theme, the property must be the empty string in that case
            "picocssTheme" to picocssTheme.takeIf { it != "azure" }?.let { ".$it" }.orEmpty(),
        )
    }
    from(jserdeDocsProject.layout.buildDirectory.dir("docs")) {
        into("docs")
        exclude("**/.asciidoctor")
    }
    into(layout.buildDirectory.dir("website"))
}

tasks.assemble {
    dependsOn(website)
}
