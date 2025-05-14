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

package jserde.core.ser;

import com.google.errorprone.annotations.MustBeClosed;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import jserde.core.DataType;
import jserde.io.LightCharArrayWriter;

/**
 * Format-specific value {@link DataWriter}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataValueWriter extends DataWriter {
    /**
     * Serializes a {@link DataType#NULL}.
     *
     * @throws IOException if an I/O error occurs
     */
    void serializeNull() throws IOException;

    /**
     * Serializes the given {@link DataType#BOOLEAN}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeBoolean(boolean value) throws IOException;

    /**
     * Serializes the given {@link DataType#BYTE}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeByte(byte value) throws IOException;

    /**
     * Serializes the given {@link DataType#SHORT}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeShort(short value) throws IOException;

    /**
     * Serializes the given {@link DataType#INT}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeInt(int value) throws IOException;

    /**
     * Serializes the given {@link DataType#LONG}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeLong(long value) throws IOException;

    /**
     * Serializes the given {@link DataType#BIG_INTEGER}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeBigInteger(BigInteger value) throws IOException;

    /**
     * Serializes the given {@link DataType#FLOAT}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeFloat(float value) throws IOException;

    /**
     * Serializes the given {@link DataType#DOUBLE}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeDouble(double value) throws IOException;

    /**
     * Serializes the given {@link DataType#BIG_DECIMAL}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeBigDecimal(BigDecimal value) throws IOException;

    /**
     * Serializes the given {@link DataType#CHAR}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeChar(char value) throws IOException;

    /**
     * Serializes a {@link DataType#STRING}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeString(String value) throws IOException;

    /**
     * Serializes a {@link DataType#STRING} given as a {@link Reader}.
     *
     * <p>Implementing classes that do not require an instance of {@link String} should override this method.
     *
     * <p>Implementations should close the reader.
     *
     * <p>The default implementation invokes {@link Reader#transferTo(Writer)} on the reader and delegates to {@link #serializeString(String)}.
     * The reader is properly closed.
     *
     * @param reader the reader
     * @param lengthHint the length of the string,
     * or -1 if it is not known
     * @throws IOException if an I/O error occurs
     */
    default void serializeString(Reader reader, int lengthHint) throws IOException {
        final String value;
        try (
            reader;
            // TODO: Extract constant for default initial capacity
            var writer = new LightCharArrayWriter(lengthHint > 0 ? lengthHint : 100)
        ) {
            reader.transferTo(writer);
            value = writer.toString();
        }
        serializeString(value);
    }

    /**
     * Serializes a {@link DataType#BYTE_ARRAY}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeByteArray(byte[] value) throws IOException;

    /**
     * Serializes a {@link DataType#BYTE_ARRAY} given as a {@link InputStream}.
     *
     * <p>Implementing classes that do not require a {@code byte} array should override this method.
     *
     * <p>Implementations should close the input stream.
     *
     * <p>The default implementation invokes {@link InputStream#transferTo(OutputStream)} on the input stream and delegates to {@link #serializeByteArray(byte[])}.
     * The input stream is properly closed.
     *
     * @param input the input stream
     * @param lengthHint the length of the byte array,
     * or -1 if it is not known
     * @throws IOException if an I/O error occurs
     */
    default void serializeByteArray(InputStream input, int lengthHint) throws IOException {
        final byte[] value;
        try (
            input;
            // TODO: Extract constant for default initial capacity
            // TODO #optimization: Use a light class from jserde.io when available
            var output = new ByteArrayOutputStream(lengthHint > 0 ? lengthHint : 100)
        ) {
            input.transferTo(output);
            value = output.toByteArray();
        }
        serializeByteArray(value);
    }

    /**
     * Serializes a {@link DataType#LOCAL_DATE}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeLocalDate(LocalDate value) throws IOException;

    /**
     * Serializes a {@link DataType#LOCAL_TIME}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeLocalTime(LocalTime value) throws IOException;

    /**
     * Serializes a {@link DataType#LOCAL_DATE_TIME}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeLocalDateTime(LocalDateTime value) throws IOException;

    /**
     * Serializes an {@link DataType#OFFSET_DATE_TIME}.
     *
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs
     */
    void serializeOffsetDateTime(OffsetDateTime value) throws IOException;

    /**
     * Starts serializing a {@link DataType#SEQUENCE}.
     *
     * <p>The returned {@link DataSequenceWriter} must be properly closed.
     *
     * @param sizeHint the size of the sequence,
     * or -1 if it is not known
     * @return a sequence writer
     * @throws IOException if an I/O error occurs
     */
    @MustBeClosed
    DataSequenceWriter serializeSequence(int sizeHint) throws IOException;

    /**
     * Starts serializing a {@link DataType#MAP}.
     *
     * <p>The returned {@link DataMapWriter} must be properly closed.
     *
     * @param sizeHint the size of the map,
     * or -1 if it is not known
     * @return a map writer
     * @throws IOException if an I/O error occurs
     */
    @MustBeClosed
    DataMapWriter serializeMap(int sizeHint) throws IOException;

    /**
     * Starts serializing a {@link DataType#STRUCT}.
     *
     * <p>The returned {@link DataStructWriter} must be properly closed.
     *
     * @param sizeHint the number of fields in the struct,
     * or -1 if it is not known
     * @return a struct writer
     * @throws IOException if an I/O error occurs
     */
    @MustBeClosed
    DataStructWriter serializeStruct(int sizeHint) throws IOException;
}
