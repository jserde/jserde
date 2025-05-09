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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import jserde.core.de.DataValueReader;
import jserde.io.LightCharArrayReader;

/**
 * Factory of {@link DataValueReader}s for a text format.
 *
 * @author Laurent Pireyn
 */
public interface TextValueReaderFactory {
    /**
     * Creates a {@link DataValueReader} on the given {@link Reader}.
     *
     * <p>Closing the returned value reader should close the reader.
     *
     * @param reader the reader
     * @return a value reader on {@code reader}
     * @throws IOException if an I/O error occurs
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Reader subclasses require to be closed
    DataValueReader createDataValueReader(Reader reader) throws IOException;

    /**
     * Creates a {@link DataValueReader} on the given {@link String}.
     *
     * <p>The default implementation invokes {@link #createDataValueReader(Reader)} with a {@link StringReader} on the given string.
     *
     * @param string the string
     * @return a value reader on {@code string}
     * @throws IOException if an I/O error occurs
     */
    default DataValueReader createDataValueReader(String string) throws IOException {
        // TODO #optimization: Use LightStringReader from jserde-io when available
        return createDataValueReader(new StringReader(string));
    }

    /**
     * Creates a {@link DataValueReader} on the given {@link CharSequence}.
     *
     * <p>The default implementation invokes {@link #createDataValueReader(String)} with the string representation of the given sequence.
     *
     * @param sequence the sequence
     * @return a value reader on {@code sequence}
     * @throws IOException if an I/O error occurs
     */
    default DataValueReader createDataValueReader(CharSequence sequence) throws IOException {
        return createDataValueReader(sequence.toString());
    }

    /**
     * Creates a {@link DataValueReader} on the given {@code char} array.
     *
     * <p>The default implementation invokes {@link #createDataValueReader(Reader)} with a {@link LightCharArrayReader} on the given array.
     *
     * @param array the array
     * @return a value reader on {@code array}
     * @throws IOException if an I/O error occurs
     */
    default DataValueReader createDataValueReader(char[] array) throws IOException {
        return createDataValueReader(new LightCharArrayReader(array));
    }
}
