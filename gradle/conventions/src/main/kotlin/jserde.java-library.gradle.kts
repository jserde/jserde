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

import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway

plugins {
    id("jserde.base")
    id("java-library")
    id("net.ltgt.errorprone")
    id("net.ltgt.nullaway")
    id("checkstyle")
    id("jacoco")
    id("org.barfuin.gradle.jacocolog")
}

val javaVersion = JavaLanguageVersion.of(providers.gradleProperty("jserde.java.version").get())
val checkstyleVersion = "10.23.0"
val errorproneVersion = "2.36.0"
val jacocoVersion = "0.8.12"
val junitVersion = "5.12.0"
val jspecifyVersion = "1.0.0"
val nullawayVersion = "0.12.4"

dependencies {
    api("org.jspecify:jspecify:$jspecifyVersion")
    compileOnly("com.google.errorprone:error_prone_annotations:$errorproneVersion")
    errorprone("com.google.errorprone:error_prone_core:$errorproneVersion")
    errorprone("com.uber.nullaway:nullaway:$nullawayVersion")
}

java {
    toolchain {
        languageVersion.set(javaVersion)
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.javaModuleVersion.set(provider { project.version.toString() })
    options.errorprone {
        nullaway {
            error()
            onlyNullMarked.set(true)
            checkOptionalEmptiness.set(true)
            checkContracts.set(true)
            handleTestAssertionLibraries.set(true)
            excludedFieldAnnotations.addAll(
                "org.junit.jupiter.api.io.TempDir"
            )
            customInitializerAnnotations.addAll(
                "org.junit.jupiter.api.BeforeAll",
                "org.junit.jupiter.api.BeforeEach"
            )
        }
    }
}

checkstyle {
    toolVersion = checkstyleVersion
}

tasks.withType<Checkstyle> {
    // NOTE: Checkstyle 10.23.0 doesn't support module-info.java files (https://github.com/checkstyle/checkstyle/issues/8240)
    exclude("**/module-info.java")
    reports {
        xml.required.set(true)
        html.required.set(true)
        sarif.required.set(true)
    }
}

testing {
    jvmTestSuites {
        dependencies {
            implementation(platform("org.junit:junit-bom:$junitVersion"))
            implementation("org.junit.jupiter:junit-jupiter")
            implementation("org.junit.jupiter:junit-jupiter-params")
            runtimeOnly("org.junit.platform:junit-platform-reporting")
        }
        targets.configureEach {
            testTask {
                useJUnitPlatform()
                jvmArgs("-ea")
                systemProperty("java.io.tmpdir", layout.buildDirectory.dir("tmp/$name").get().asFile.path)
                systemProperty("junit.platform.reporting.open.xml.enabled", true)
                systemProperty("junit.platform.reporting.output.dir", java.testResultsDir.dir(name).get().asFile.path)
            }
        }
        tasks.getByName<JavaCompile>(sources.compileJavaTaskName) {
            options.errorprone {
                // Allow JUnit Jupiter @Nested inner classes
                // https://errorprone.info/bugpattern/ClassCanBeStatic
                disable("ClassCanBeStatic")
            }
        }
    }
}

jacoco {
    toolVersion = jacocoVersion
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Javadoc> {
    standardOptions {
        version()
        author()
        linkSource()
        links?.add("https://docs.oracle.com/en/java/javase/${javaVersion.asInt()}/docs/api")
        links?.add("https://javadoc.io/doc/com.google.errorprone/error_prone_annotations/$errorproneVersion")
        links?.add("https://javadoc.io/doc/org.jspecify/jspecify/$jspecifyVersion")
    }
}
