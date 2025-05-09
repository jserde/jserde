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
    id("base")
}

group = "jserde"
version = providers.gradleProperty("jserde.version").get()

val configurations by tasks.registering(DefaultTask::class) {
    description = "Displays the configurations of the project."
    group = HelpTasksPlugin.HELP_GROUP
    doLast {
        project.configurations.forEach { configuration ->
            println("Configuration: ${configuration.name}")
            if (!(configuration.description.isNullOrBlank())) {
                println("  Description: ${configuration.description}")
            }
            println("  Can be consumed: ${configuration.isCanBeConsumed}")
            println("  Can be resolved: ${configuration.isCanBeResolved}")
            println("  Can be declared: ${configuration.isCanBeDeclared}")
            println("  Transitive: ${configuration.isTransitive}")
            println("  Visible: ${configuration.isVisible}")
            if (!(configuration.attributes.isEmpty())) {
                println("  Attributes:")
                configuration.attributes.keySet().forEach { attribute ->
                    println("    - ${attribute.name} (${attribute.type.name}): ${configuration.attributes.getAttribute(attribute)}")
                }
            }
            if (configuration.extendsFrom.isNotEmpty()) {
                println("  Extends from:")
                configuration.extendsFrom.forEach { extendedConfiguration ->
                    println("    - ${extendedConfiguration.name}")
                }
            }
            if (configuration.dependencies.isNotEmpty()) {
                println("  Dependencies:")
                configuration.dependencies.forEach { dependency ->
                    println("    - $dependency")
                }
            }
            println()
        }
    }
}

// Make archives reproducible
tasks.withType<AbstractArchiveTask> {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}
