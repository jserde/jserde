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

description = "Test utilities used by the JSerde projects."

dependencies {
    // TODO: Use the same version as the jserde.java-library convention
    api(platform("org.junit:junit-bom:5.12.0"))
    api("org.junit.jupiter:junit-jupiter")
    api("org.junit.jupiter:junit-jupiter-params")
}
