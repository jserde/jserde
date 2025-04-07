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
    id("me.champeau.jmh")
    id("io.morethan.jmhreport")
}

jmh {
    jmhVersion.set("1.37")
    includeTests.set(false)
    // The result format must be JSON for the JMH report
    resultFormat.set("JSON")
    // Profiler - JFR
    if (providers.gradleProperty("jserde.jmh.jfrProfiler.enabled").orNull?.toBooleanStrictOrNull() == true) {
        val outputDir = layout.buildDirectory.dir("reports/jmh/jfr").get().asFile.path
        profilers.add("jfr:dir=$outputDir")
    }
    // Profiler - Async-profiler
    if (providers.gradleProperty("jserde.jmh.asyncProfiler.enabled").orNull?.toBooleanStrictOrNull() == true) {
        val libPath = providers.gradleProperty("jserde.jmh.asyncProfiler.libPath").get()
        val outputDir = layout.buildDirectory.dir("reports/jmh/async-profiler").get().asFile.path
        profilers.add("async:libPath=$libPath;output=flamegraph;dir=$outputDir")
    }
}

val jmhReportOutputDir = layout.buildDirectory.dir("reports/jmh")

jmhReport {
    jmhResultPath = tasks.jmh.get().resultsFile.get().asFile.path
    jmhReportOutput = jmhReportOutputDir.get().asFile.path
}

tasks.jmh {
    finalizedBy(tasks.jmhReport)
}

tasks.jmhReport {
    doFirst {
        // JmhReportTask crashes if the output directory does not exist
        mkdir(jmhReportOutputDir)
    }
}
