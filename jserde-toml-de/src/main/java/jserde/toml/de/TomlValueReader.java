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

package jserde.toml.de;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.io.LightCharArrayReader;
import jserde.toml.TomlFormat;
import org.jspecify.annotations.Nullable;

/**
 * TOML {@link DataValueReader}.
 *
 * @author Laurent Pireyn
 */
// TODO: Remove this annotation
@SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
public final class TomlValueReader implements DataValueReader {
    private final Reader reader;

    /**
     * Creates a new {@code TomlValueReader} on the given {@link Reader}.
     *
     * @param reader the reader
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Reader subclasses require to be closed
    public TomlValueReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * Creates a new {@code TomlValueReader} on the given {@code char} array.
     *
     * @param array the array
     */
    public TomlValueReader(char[] array) {
        this(new LightCharArrayReader(array));
    }

    /**
     * Creates a new {@code TomlValueReader} on the given {@link String}.
     *
     * @param string the string
     */
    public TomlValueReader(String string) {
        // TODO #optimization: Use LightStringReader from jserde-io when available
        this(new StringReader(string));
    }

    /**
     * Creates a new {@code TomlValueReader} on the given {@link InputStream},
     * using {@link TomlFormat#CHARSET}.
     *
     * @param input the input stream
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all InputStream subclasses require to be closed
    public TomlValueReader(InputStream input) {
        this(new InputStreamReader(input, TomlFormat.CHARSET));
    }

    // Reader-related methods

    @Override
    public void close() throws IOException {
        reader.close();
    }

    // DataValueReader methods

    @Override
    public <T extends @Nullable Object> T deserializeNull(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeBoolean(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeByte(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeShort(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeInt(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeLong(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeBigInteger(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeFloat(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeDouble(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeBigDecimal(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeChar(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeString(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeByteArray(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeLocalDate(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeLocalTime(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeLocalDateTime(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeOffsetDateTime(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeSequence(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeMap(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeStruct(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeAny(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }
}
