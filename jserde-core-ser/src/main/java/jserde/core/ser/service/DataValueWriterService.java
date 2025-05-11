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

package jserde.core.ser.service;

import jserde.core.ser.factory.BinaryValueWriterFactory;
import jserde.core.ser.factory.TextValueWriterFactory;
import jserde.core.service.FormatService;
import org.jspecify.annotations.Nullable;

/**
 * Data value writer service for a format.
 *
 * <p>Implementing classes should override at least one of the {@code get*ValueWriterFactory} methods.
 *
 * @author Laurent Pireyn
 */
public interface DataValueWriterService extends FormatService {
    /**
     * Returns the {@link BinaryValueWriterFactory} for this format.
     *
     * <p>The default implementation returns {@code null}.
     *
     * @return the binary value writer factory for this format,
     * or {@code null} if this format is a text format with no default encoding
     */
    default @Nullable BinaryValueWriterFactory getBinaryValueWriterFactory() {
        return null;
    }

    /**
     * Returns the {@link TextValueWriterFactory} for this format.
     *
     * <p>The default implementation returns {@code null}.
     *
     * @return the text value writer factory for this format,
     * or {@code null} if this format is a binary format
     */
    default @Nullable TextValueWriterFactory getTextValueWriterFactory() {
        return null;
    }
}
