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

package jserde.core.de.service;

import jserde.core.de.factory.BinaryValueReaderFactory;
import jserde.core.de.factory.TextValueReaderFactory;
import jserde.core.service.FormatService;
import org.jspecify.annotations.Nullable;

/**
 * Data value reader service for a format.
 *
 * <p>Implementing classes should override at least one of the {@code get*ValueReaderFactory} methods.
 *
 * @author Laurent Pireyn
 */
public interface DataValueReaderService extends FormatService {
    /**
     * Returns the {@link BinaryValueReaderFactory} for this format.
     *
     * <p>The default implementation returns {@code null}.
     *
     * @return the binary value reader factory for this format,
     * or {@code null} if this format is a text format with no default encoding
     */
    default @Nullable BinaryValueReaderFactory getBinaryValueReaderFactory() {
        return null;
    }

    /**
     * Returns the {@link TextValueReaderFactory} for this format.
     *
     * <p>The default implementation returns {@code null}.
     *
     * @return the text value reader factory for this format,
     * or {@code null} if this format is a binary format
     */
    default @Nullable TextValueReaderFactory getTextValueReaderFactory() {
        return null;
    }
}
