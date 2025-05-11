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

package jserde.json5;

import com.google.errorprone.annotations.Immutable;

/**
 * Utilities related to the JSON5 format.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class Json5Format {
    /**
     * Canonical name of the JSON format: {@value}.
     */
    public static final String NAME = "json5";

    private Json5Format() {}
}
