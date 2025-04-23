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

val destDir = layout.buildDirectory.dir("website").get()

val websiteBase by tasks.registering(Sync::class) {
    // https://picocss.com/docs/version-picker
    val picocssTheme = "azure"
    from(layout.projectDirectory.dir("src/website"))
    into(destDir)
    expand(
        "projectVersion" to project.version.toString(),
        "picocssVersion" to "2.1.0",
        // NOTE: "azure" being the default theme, the property must be the empty string in that case
        "picocssTheme" to picocssTheme.takeIf { it != "azure" }?.let { ".$it" }.orEmpty(),
    )
}

val jserdeDocsProject = project(":jserde-docs")

val copyDocs by tasks.registering(Sync::class) {
    dependsOn(jserdeDocsProject.tasks["assemble"])
    from(jserdeDocsProject.layout.buildDirectory.dir("docs"))
    into(destDir.dir("docs"))
    exclude("**/.asciidoctor")
}

val website by tasks.registering(DefaultTask::class) {
    description = "Generates the JSerde website."
    dependsOn(
        websiteBase,
        copyDocs,
    )
}

tasks.assemble {
    dependsOn(website)
}
