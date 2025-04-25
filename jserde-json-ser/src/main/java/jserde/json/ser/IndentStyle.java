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

package jserde.json.ser;

/**
 * Indent style.
 *
 * @author Laurent Pireyn
 */
// TODO: Consider moving this enum to a common project
public enum IndentStyle {
    /**
     * Do not indent.
     */
    NONE,

    /**
     * Indent with space(s).
     */
    SPACE,

    /**
     * Indent with tab(s).
     */
    TAB
}
