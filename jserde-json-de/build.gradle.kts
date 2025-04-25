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
    id("jserde.java-library")
}

description = "JSerde deserialization components for the JSON format."

dependencies {
    api(project(":jserde-core"))
    api(project(":jserde-core-de"))
    api(project(":jserde-json"))
    implementation(project(":jserde-io"))
}

testing {
    jvmTestSuites {
        dependencies {
            implementation(project(":jserde-test"))
        }
    }
    suites {
        val test by getting(JvmTestSuite::class)
        // JSONTestSuite
        // https://github.com/nst/JSONTestSuite
        val jsonTestSuite by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }
            targets.all {
                testTask {
                    // Don't bother running this test suite if the unit tests fail
                    dependsOn(test)
                }
            }
        }
        tasks.check {
            dependsOn(jsonTestSuite)
        }
    }
}
