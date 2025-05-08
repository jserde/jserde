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

package jserde.toml;

/**
 * TOML value type.
 *
 * @author Laurent Pireyn
 */
public enum TomlValueType {
    /**
     * TOML boolean.
     */
    BOOLEAN,

    /**
     * TOML integer.
     */
    INTEGER,

    /**
     * TOML float.
     */
    FLOAT,

    /**
     * TOML string.
     */
    STRING,

    /**
     * TOML offset date-time.
     */
    OFFSET_DATE_TIME,

    /**
     * TOML local date-time.
     */
    LOCAL_DATE_TIME,

    /**
     * TOML local date.
     */
    LOCAL_DATE,

    /**
     * TOML local time.
     */
    LOCAL_TIME,

    /**
     * TOML array.
     */
    ARRAY,

    /**
     * TOML table.
     */
    TABLE
}
