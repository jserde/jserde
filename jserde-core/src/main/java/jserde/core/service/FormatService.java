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

package jserde.core.service;

import java.util.Set;

/**
 * Service for a format.
 *
 * @author Laurent Pireyn
 */
public interface FormatService {
    /**
     * Returns the canonical name of this format.
     *
     * @return the canonical name of this format
     */
    String getFormat();

    /**
     * Returns the set of media types supported by this format.
     *
     * @return the set of media types supported by this format
     */
    Set<String> getSupportedMediaTypes();

    /**
     * Returns the set of file extensions supported by this format.
     *
     * <p>The file extensions should not start with a dot,
     * although service locators are expected to be flexible.
     *
     * @return the set of file extensions supported by this format
     */
    Set<String> getSupportedFileExtensions();
}
