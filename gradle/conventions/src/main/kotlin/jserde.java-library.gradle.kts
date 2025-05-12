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

import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway

plugins {
    id("jserde.base")
    id("java-library")
    id("net.ltgt.errorprone")
    id("net.ltgt.nullaway")
    id("com.github.spotbugs")
    id("checkstyle")
    id("jacoco")
    id("org.barfuin.gradle.jacocolog")
    id("jserde.maven-publish")
}

val javaVersion = JavaLanguageVersion.of(providers.gradleProperty("jserde.java.version").get())
val checkstyleVersion = "10.23.1"
val errorproneVersion = "2.38.0"
val jacocoVersion = "0.8.12"
val junitVersion = "5.12.0"
val jspecifyVersion = "1.0.0"
val nullawayVersion = "0.12.7"
val spotbugsVersion = "4.9.3"

dependencies {
    api("org.jspecify:jspecify:$jspecifyVersion")
    compileOnly("com.google.errorprone:error_prone_annotations:$errorproneVersion")
    compileOnly("com.github.spotbugs:spotbugs-annotations:$spotbugsVersion")
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

spotbugs {
    toolVersion.set(spotbugsVersion)
    excludeFilter.set(rootProject.layout.projectDirectory.file("config/spotbugs/spotbugs-exclude.xml"))
}

tasks.withType<SpotBugsTask> {
    reports.create("xml") {
        required.set(true)
    }
    reports.create("html") {
        required.set(true)
    }
    reports.create("sarif") {
        required.set(true)
    }
}

val spotbugs by tasks.registering(DefaultTask::class) {
    description = "Runs the SpotBugs tasks of project '${project.path}'."
    group = JavaBasePlugin.VERIFICATION_GROUP
    dependsOn(tasks.withType<SpotBugsTask>())
}

checkstyle {
    toolVersion = checkstyleVersion
}

tasks.withType<Checkstyle> {
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
                systemProperty("java.util.logging.config.file", rootProject.layout.projectDirectory.file("config/logging/logging-test.properties").asFile.path)
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
        links?.add("https://javadoc.io/doc/com.github.spotbugs/spotbugs-annotations/$spotbugsVersion")
        links?.add("https://javadoc.io/doc/org.jspecify/jspecify/$jspecifyVersion")
    }
}

publishing {
    publications {
        val main by registering(MavenPublication::class) {
            from(components["java"])
        }
    }
}
