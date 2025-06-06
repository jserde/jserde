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

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://gitlab.com/api/v4/groups/smartefact/-/packages/maven")
    }
    includeBuild("gradle/conventions")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "jserde"

include(
    ":jserde-annotations",
    ":jserde-bom",
    ":jserde-core",
    ":jserde-core-de",
    ":jserde-core-ser",
    ":jserde-docs",
    ":jserde-io",
    ":jserde-javacc",
    ":jserde-json",
    ":jserde-json-de",
    ":jserde-json-model",
    ":jserde-json-ser",
    ":jserde-json5",
    ":jserde-json5-de",
    ":jserde-json5-ser",
    ":jserde-service-de",
    ":jserde-service-ser",
    ":jserde-test",
    ":jserde-toml",
    ":jserde-toml-de",
    ":jserde-toml-ser",
    ":jserde-website",
)
