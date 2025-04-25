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

package jserde.json;

/**
 * JSON value type.
 *
 * @author Laurent Pireyn
 */
public enum JsonValueType {
    /**
     * JSON null.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-3">RFC 8259 - 3</a>
     */
    NULL,

    /**
     * JSON boolean.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-3">RFC 8259 - 3</a>
     */
    BOOLEAN,

    /**
     * JSON number.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-6">RFC 8259 - 6</a>
     */
    NUMBER,

    /**
     * JSON string.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-7">RFC 8259 - 7</a>
     */
    STRING,

    /**
     * JSON array.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-5">RFC 8259 - 5</a>
     */
    ARRAY,

    /**
     * JSON object.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-4">RFC 8259 - 4</a>
     */
    OBJECT
}
