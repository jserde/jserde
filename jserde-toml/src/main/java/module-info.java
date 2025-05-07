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
 * JSerde components for the TOML format.
 *
 * <p>The TOML format is specified by
 * <a href="https://toml.io/en/v1.0.0">TOML v1.0.0</a>.
 *
 * @author Laurent Pireyn
 */
@NullMarked
module jserde.toml {
    requires transitive org.jspecify;
    requires static com.google.errorprone.annotations;
    exports jserde.toml;
}
