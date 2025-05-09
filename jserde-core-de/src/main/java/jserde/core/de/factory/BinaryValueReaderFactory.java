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

package jserde.core.de.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import jserde.core.de.DataValueReader;

/**
 * Factory of {@link DataValueReader}s for a binary format.
 *
 * @author Laurent Pireyn
 */
public interface BinaryValueReaderFactory {
    /**
     * Creates a {@link DataValueReader} on the given {@link InputStream}.
     *
     * <p>Closing the returned value reader should close the input stream.
     *
     * @param input the input stream
     * @return a value reader on {@code input}
     * @throws IOException if an I/O error occurs
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all InputStream subclasses require to be closed
    DataValueReader createDataValueReader(InputStream input) throws IOException;

    /**
     * Creates a {@link DataValueReader} on the given {@code byte} array.
     *
     * <p>The default implementation invokes {@link #createDataValueReader(InputStream)} with a {@link ByteArrayInputStream} on the given array.
     *
     * @param array the array
     * @return a value reader on {@code array}
     * @throws IOException if an I/O error occurs
     */
    default DataValueReader createDataValueReader(byte[] array) throws IOException {
        // TODO #optimization: Use LightByteArrayInputStream from jserde-io when available
        return createDataValueReader(new ByteArrayInputStream(array));
    }
}
