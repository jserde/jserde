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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utilities related to the TOML format.
 *
 * @author Laurent Pireyn
 */
public final class TomlFormat {
    /**
     * Canonical name of the TOML format: {@value}.
     */
    public static final String NAME = "toml";

    /**
     * {@link Charset} used by TOML text: UTF-8.
     *
     * @see <a href="https://toml.io/en/v1.0.0#spec">TOML 1.0.0 - Spec</a>
     */
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Media type for TOML text: {@value}.
     *
     * @see <a href="https://toml.io/en/v1.0.0#mime-type">TOML 1.0.0 - MIME Type</a>
     */
    public static final String MEDIA_TYPE = "application/toml";

    /**
     * File extension for TOML text: {@value}.
     *
     * @see <a href="https://toml.io/en/v1.0.0#filename-extension">TOML 1.0.0 - Filename Extension</a>
     */
    public static final String FILE_EXTENSION = "toml";

    private TomlFormat() {}
}
