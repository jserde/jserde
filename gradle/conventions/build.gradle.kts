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
    `kotlin-dsl`
}

dependencies {
    val roasterVersion = "2.30.1.Final"
    api("com.github.spotbugs:com.github.spotbugs.gradle.plugin:6.1.11")
    api("io.morethan.jmhreport:io.morethan.jmhreport.gradle.plugin:0.9.6")
    api("me.champeau.jmh:jmh-gradle-plugin:0.7.3")
    api("net.ltgt.gradle:gradle-errorprone-plugin:4.2.0")
    api("net.ltgt.gradle:gradle-nullaway-plugin:2.2.0")
    api("org.asciidoctor.jvm.convert:org.asciidoctor.jvm.convert.gradle.plugin:4.0.4")
    api("org.barfuin.gradle.jacocolog:gradle-jacoco-log:3.1.0")
    api("org.jboss.forge.roaster:roaster-api:$roasterVersion")
    runtimeOnly("org.jboss.forge.roaster:roaster-jdt:$roasterVersion")
}
