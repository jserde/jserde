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

package jserde.toml.ser;

import com.google.errorprone.annotations.MustBeClosed;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.DataValueWriter;
import jserde.toml.TomlFormat;

/**
 * JSON {@link DataValueWriter}.
 *
 * @author Laurent Pireyn
 */
public final class TomlValueWriter implements DataValueWriter {
    private final Writer writer;

    /**
     * Creates a new {@code TomlValueWriter} on the given {@link Writer}.
     *
     * @param writer the writer
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public TomlValueWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Creates a new {@code TomlValueWriter} on the given {@link OutputStream},
     * using {@link TomlFormat#CHARSET}.
     *
     * @param output the output stream
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    public TomlValueWriter(OutputStream output) {
        this(new OutputStreamWriter(output, TomlFormat.CHARSET));
    }

    // DataValueWriter methods

    @Override
    public void serializeNull() throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeBoolean(boolean value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeByte(byte value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeShort(short value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeInt(int value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeLong(long value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeBigInteger(BigInteger value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeFloat(float value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeDouble(double value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeBigDecimal(BigDecimal value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeChar(char value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeString(String value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeString(Reader reader, int lengthHint) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeByteArray(byte[] value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int size) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    @MustBeClosed
    public DataMapWriter serializeMap(int size) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int fieldCount) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
