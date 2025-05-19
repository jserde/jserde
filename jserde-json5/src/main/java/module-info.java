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

import org.jspecify.annotations.NullMarked;

/**
 * JSerde components for the JSON5 format.
 *
 * <p>The JSON5 format is specified by
 * <a href="https://spec.json5.org/">The JSON5 Data Interchange Format</a>.
 *
 * @author Laurent Pireyn
 */
@NullMarked
// The 5 in JSON5 is part of the name, not a version
@SuppressWarnings("module")
module jserde.json5 {
    requires transitive jserde.core;
    requires transitive org.jspecify;
    requires static com.github.spotbugs.annotations;
    requires static com.google.errorprone.annotations;
    exports jserde.json5;
}
