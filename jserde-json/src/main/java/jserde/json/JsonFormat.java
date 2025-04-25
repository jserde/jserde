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

import com.google.errorprone.annotations.Immutable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utilities related to the JSON format.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonFormat {
    /**
     * Default {@link Charset} used by JSON text: UTF-8.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-8.1">RFC 8259 - 8.1</a>
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Media type for JSON text: {@value}.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-11">RFC 8259 - 11</a>
     */
    public static final String MEDIA_TYPE = "application/json";

    /**
     * File extension for JSON text: {@value}.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc8259#section-11">RFC 8259 - 11</a>
     */
    public static final String FILE_EXTENSION = "json";

    private JsonFormat() {}
}
