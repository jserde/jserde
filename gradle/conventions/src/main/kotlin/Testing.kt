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

import org.gradle.api.DomainObjectCollection
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.plugins.JvmTestSuitePlugin
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestSuite
import org.gradle.testing.base.TestingExtension

const val ARCH_TEST = "archTest"
const val FUNCTIONAL_TEST = "functionalTest"
const val INTEGRATION_TEST = "integrationTest"
const val UNIT_TEST = JvmTestSuitePlugin.DEFAULT_TEST_SUITE_NAME

/**
 * Configures the JVM test suites.
 */
fun TestingExtension.jvmTestSuites(configure: JvmTestSuite.() -> Unit): DomainObjectCollection<JvmTestSuite> =
    suites.withType<JvmTestSuite>(configure)

/**
 * Configures the JVM test suite with the given name.
 */
fun NamedDomainObjectCollection<out TestSuite>.jvmTestSuite(name: String, configure: JvmTestSuite.() -> Unit): JvmTestSuite =
    getByName(name, JvmTestSuite::class, configure)

/**
 * Configures the architecture tests.
 */
fun NamedDomainObjectCollection<out TestSuite>.archTest(configure: JvmTestSuite.() -> Unit): JvmTestSuite =
    jvmTestSuite(ARCH_TEST, configure)

/**
 * Configures the functional tests.
 */
fun NamedDomainObjectCollection<out TestSuite>.functionalTest(configure: JvmTestSuite.() -> Unit): JvmTestSuite =
    jvmTestSuite(FUNCTIONAL_TEST, configure)

/**
 * Configures the integration tests.
 */
fun NamedDomainObjectCollection<out TestSuite>.integrationTest(configure: JvmTestSuite.() -> Unit): JvmTestSuite =
    jvmTestSuite(INTEGRATION_TEST, configure)

/**
 * Configures the unit tests.
 */
fun NamedDomainObjectCollection<out TestSuite>.unitTest(configure: JvmTestSuite.() -> Unit): JvmTestSuite =
    jvmTestSuite(UNIT_TEST, configure)
