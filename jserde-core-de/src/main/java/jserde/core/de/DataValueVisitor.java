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

package jserde.core.de;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import java.util.Set;
import jserde.core.DataType;
import jserde.io.LightCharArrayWriter;
import org.jspecify.annotations.Nullable;

/**
 * Visitor that walks through data to produce a value.
 *
 * @author Laurent Pireyn
 * @param <T> the type of values produced by this visitor
 */
public interface DataValueVisitor<T extends @Nullable Object> {
    /**
     * Produces a value from a {@link DataType#NULL}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitNull() throws IOException {
        throw unsupportedTypeException(DataType.NULL);
    }

    /**
     * Produces a value from a {@link DataType#BOOLEAN}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitBoolean(boolean value) throws IOException {
        throw unsupportedTypeException(DataType.BOOLEAN);
    }

    /**
     * Produces a value from a {@link DataType#BYTE}.
     *
     * <p>The default implementation delegates to {@link #visitShort(short)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitByte(byte value) throws IOException {
        try {
            return visitShort(value);
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.BYTE);
        }
    }

    /**
     * Produces a value from a {@link DataType#SHORT}.
     *
     * <p>The default implementation delegates to {@link #visitInt(int)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitShort(short value) throws IOException {
        try {
            return visitInt(value);
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.SHORT);
        }
    }

    /**
     * Produces a value from an {@link DataType#INT}.
     *
     * <p>The default implementation delegates to {@link #visitLong(long)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitInt(int value) throws IOException {
        try {
            return visitLong(value);
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.INT);
        }
    }

    /**
     * Produces a value from a {@link DataType#LONG}.
     *
     * <p>The default implementation delegates to {@link #visitBigInteger(BigInteger)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitLong(long value) throws IOException {
        try {
            return visitBigInteger(BigInteger.valueOf(value));
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.LONG);
        }
    }

    /**
     * Produces a value from a {@link DataType#BIG_INTEGER}.
     *
     * <p>The default implementation delegates to {@link #visitBigDecimal(BigDecimal)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitBigInteger(BigInteger value) throws IOException {
        try {
            return visitBigDecimal(new BigDecimal(value));
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.BIG_INTEGER);
        }
    }

    /**
     * Produces a value from a {@link DataType#FLOAT}.
     *
     * <p>The default implementation delegates to {@link #visitBigDecimal(BigDecimal)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitFloat(float value) throws IOException {
        try {
            return visitBigDecimal(new BigDecimal(value));
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.FLOAT);
        }
    }

    /**
     * Produces a value from a {@link DataType#DOUBLE}.
     *
     * <p>The default implementation delegates to {@link #visitBigDecimal(BigDecimal)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitDouble(double value) throws IOException {
        try {
            return visitBigDecimal(new BigDecimal(value));
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.DOUBLE);
        }
    }

    /**
     * Produces a value from a {@link DataType#BIG_DECIMAL}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitBigDecimal(BigDecimal value) throws IOException {
        throw unsupportedTypeException(DataType.BIG_DECIMAL);
    }

    /**
     * Produces a value from a {@link DataType#CHAR}.
     *
     * <p>The default implementation delegates to {@link #visitString(String)} with a one-character string.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitChar(char value) throws IOException {
        try {
            return visitString(String.valueOf(value));
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.CHAR);
        }
    }

    /**
     * Produces a value from a {@link DataType#STRING} given as a {@link String}.
     *
     * <p>Implementing classes that override {@link #visitString(Reader, int)} should also override this method.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitString(String value) throws IOException {
        throw unsupportedTypeException(DataType.STRING);
    }

    /**
     * Produces a value from a {@link DataType#STRING} given as a {@link Reader}.
     *
     * <p>Implementing classes that do not require an instance of {@link String} should override this method.
     *
     * <p>Implementations should close the reader.
     *
     * <p>The default implementation invokes {@link Reader#transferTo(Writer)} on the reader and delegates to {@link #visitString(String)}.
     * The reader is properly closed.
     *
     * @param reader the reader
     * @param lengthHint the length of the string,
     * or -1 if it is not known
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitString(Reader reader, int lengthHint) throws IOException {
        final String value;
        try (
            reader;
            // TODO: Extract constant for default initial capacity
            var writer = new LightCharArrayWriter(lengthHint > 0 ? lengthHint : 100)
        ) {
            reader.transferTo(writer);
            value = writer.toString();
        }
        return visitString(value);
    }

    /**
     * Produces a value from a {@link DataType#BYTE_ARRAY} given as a {@code byte} array.
     *
     * <p>Implementing classes that override {@link #visitByteArray(InputStream, int)} should also override this method.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitByteArray(byte[] value) throws IOException {
        throw unsupportedTypeException(DataType.BYTE_ARRAY);
    }

    /**
     * Produces a value from a {@link DataType#BYTE_ARRAY} given as an {@link InputStream}.
     *
     * <p>Implementing classes that do not require a {@code byte} array should override this method.
     *
     * <p>Implementations should close the input stream.
     *
     * <p>The default implementation invokes {@link InputStream#transferTo(OutputStream)} on the input stream and delegates to {@link #visitByteArray(byte[])}.
     * The input stream is properly closed.
     *
     * @param input the input stream
     * @param lengthHint the length of the byte array,
     * or -1 if it is not known
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitByteArray(InputStream input, int lengthHint) throws IOException {
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
        return visitByteArray(value);
    }

    /**
     * Produces a value from a {@link DataType#LOCAL_DATE} given as a {@link LocalDate}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitLocalDate(LocalDate value) throws IOException {
        throw unsupportedTypeException(DataType.LOCAL_DATE);
    }

    /**
     * Produces a value from a {@link DataType#LOCAL_TIME} given as a {@link LocalTime}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitLocalTime(LocalTime value) throws IOException {
        throw unsupportedTypeException(DataType.LOCAL_TIME);
    }

    /**
     * Produces a value from a {@link DataType#LOCAL_DATE_TIME} given as a {@link LocalDateTime}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitLocalDateTime(LocalDateTime value) throws IOException {
        throw unsupportedTypeException(DataType.LOCAL_DATE_TIME);
    }

    /**
     * Produces a value from a {@link DataType#OFFSET_DATE_TIME} given as a {@link OffsetDateTime}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param value the value
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitOffsetDateTime(OffsetDateTime value) throws IOException {
        throw unsupportedTypeException(DataType.OFFSET_DATE_TIME);
    }

    /**
     * Produces a value from a {@link DataType#SEQUENCE}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param reader the sequence reader
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitSequence(DataSequenceReader reader) throws IOException {
        throw unsupportedTypeException(DataType.SEQUENCE);
    }

    /**
     * Produces a value from a {@link DataType#MAP}.
     *
     * <p>The default implementation throws {@link #unsupportedTypeException(DataType)}.
     *
     * @param reader the map reader
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    default T visitMap(DataMapReader reader) throws IOException {
        throw unsupportedTypeException(DataType.MAP);
    }

    /**
     * Produces a value from a {@link DataType#STRUCT}.
     *
     * <p>The default implementation delegates to {@link #visitMap(DataMapReader)},
     * passing the struct reader as a map reader (see {@link DataStructReader#asMapReader()}).
     *
     * @param reader the struct reader
     * @return the value produced by this visitor
     * @throws IOException if an I/O error occurs
     */
    @SuppressFBWarnings("LEST_LOST_EXCEPTION_STACK_TRACE")
    default T visitStruct(DataStructReader reader) throws IOException {
        try {
            return visitMap(reader.asMapReader());
        } catch (InvalidValueException e) {
            throw unsupportedTypeException(DataType.STRUCT);
        }
    }

    /**
     * Creates the {@link InvalidValueException} thrown when a value of an unsupported data type is visited.
     *
     * <p>The default implementation creates an {@code InvalidValueException} with a message that mentions the unsupported type,
     * as well as the supported type(s) obtained from {@link #getSupportedTypes()} (if any).
     *
     * @param type the unsupported type
     * @return the {@code InvalidValueException} to throw
     */
    default InvalidValueException unsupportedTypeException(DataType type) {
        final var message = new StringBuilder(100)
            .append("The data value visitor ").append(this)
            .append(" received a value of unsupported data type ").append(type);
        final var supportedTypes = getSupportedTypes();
        assert !supportedTypes.contains(type);
        if (!supportedTypes.isEmpty()) {
            message.append(" (hint: the data value visitor supports the data type(s) ").append(supportedTypes).append(')');
        }
        return new InvalidValueException(message.toString());
    }

    /**
     * Returns the set of data types supported by this visitor.
     *
     * <p>This set is used to enrich the message of the {@link InvalidValueException}
     * created by the default implementation of {@link #unsupportedTypeException(DataType)}.
     * If it is empty, the message does not mention the supported types.
     *
     * <p>The default implementation returns {@link DataType#NONE}.
     *
     * @return the set of data types supported by this visitor
     */
    default Set<DataType> getSupportedTypes() {
        return DataType.NONE;
    }
}
