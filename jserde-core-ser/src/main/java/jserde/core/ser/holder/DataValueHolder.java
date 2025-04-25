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

package jserde.core.ser.holder;

import com.google.errorprone.annotations.ForOverride;
import com.google.errorprone.annotations.MustBeClosed;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.core.DataType;
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.DataValueWriter;
import jserde.core.ser.SerializationException;
import org.jspecify.annotations.Nullable;

/**
 * Abstract {@link DataValueWriter} that holds a value.
 *
 * @author Laurent Pireyn
 * @param <T> the type of value held by this class
 */
public abstract sealed class DataValueHolder<T extends @Nullable Object> implements DataValueWriter permits NullValueHolder, BooleanValueHolder, ByteValueHolder, ShortValueHolder, IntValueHolder, LongValueHolder, BigIntegerValueHolder, FloatValueHolder, DoubleValueHolder, BigDecimalValueHolder, CharValueHolder, StringValueHolder, ByteArrayValueHolder, SequenceValueHolder, MapValueHolder, StructValueHolder, ObjectValueHolder {
    private boolean serialized;

    @Override
    public void serializeNull() throws IOException {
        throw unsupportedTypeException(DataType.NULL);
    }

    @Override
    public void serializeBoolean(boolean value) throws IOException {
        throw unsupportedTypeException(DataType.BOOLEAN);
    }

    @Override
    public void serializeByte(byte value) throws IOException {
        throw unsupportedTypeException(DataType.BYTE);
    }

    @Override
    public void serializeShort(short value) throws IOException {
        throw unsupportedTypeException(DataType.SHORT);
    }

    @Override
    public void serializeInt(int value) throws IOException {
        throw unsupportedTypeException(DataType.INT);
    }

    @Override
    public void serializeLong(long value) throws IOException {
        throw unsupportedTypeException(DataType.LONG);
    }

    @Override
    public void serializeBigInteger(BigInteger value) throws IOException {
        throw unsupportedTypeException(DataType.BIG_INTEGER);
    }

    @Override
    public void serializeFloat(float value) throws IOException {
        throw unsupportedTypeException(DataType.FLOAT);
    }

    @Override
    public void serializeDouble(double value) throws IOException {
        throw unsupportedTypeException(DataType.DOUBLE);
    }

    @Override
    public void serializeBigDecimal(BigDecimal value) throws IOException {
        throw unsupportedTypeException(DataType.BIG_DECIMAL);
    }

    @Override
    public void serializeChar(char value) throws IOException {
        throw unsupportedTypeException(DataType.CHAR);
    }

    @Override
    public void serializeString(String value) throws IOException {
        throw unsupportedTypeException(DataType.STRING);
    }

    @Override
    public void serializeByteArray(byte[] value) throws IOException {
        throw unsupportedTypeException(DataType.BYTE_ARRAY);
    }

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int sizeHint) throws IOException {
        throw unsupportedTypeException(DataType.SEQUENCE);
    }

    @Override
    @MustBeClosed
    public DataMapWriter serializeMap(int sizeHint) throws IOException {
        throw unsupportedTypeException(DataType.MAP);
    }

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int sizeHint) throws IOException {
        throw unsupportedTypeException(DataType.STRUCT);
    }

    private SerializationException unsupportedTypeException(DataType type) {
        return new SerializationException("Unsupported data type: " + type);
    }

    final void beforeSerializeValue() throws EOFException {
        if (serialized) {
            throw new EOFException("A value has already been serialized");
        }
        serialized = true;
    }

    public final T getValue() {
        if (!serialized) {
            throw new IllegalStateException("No value serialized");
        }
        return getValueInternal();
    }

    @ForOverride
    abstract T getValueInternal();

    @Override
    public final void close() {}
}
