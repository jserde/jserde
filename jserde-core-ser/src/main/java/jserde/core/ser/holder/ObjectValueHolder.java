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

import com.google.errorprone.annotations.MustBeClosed;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a value of any data type.
 *
 * @author Laurent Pireyn
 */
public final class ObjectValueHolder extends DataValueHolder<@Nullable Object> {
    private final class SequenceWriterImpl implements DataSequenceWriter {
        private final SequenceValueHolder holder = new SequenceValueHolder();
        private final DataSequenceWriter writer;

        @MustBeClosed
        // TODO: Don't suppress MustBeClosedChecker when it supports this flow
        @SuppressWarnings("MustBeClosedChecker")
        SequenceWriterImpl(int sizeHint) throws IOException {
            writer = holder.serializeSequence(sizeHint);
        }

        @Override
        public <T extends @Nullable Object> void serializeElement(T value, ValueSerializer<? super T> serializer) throws IOException {
            writer.serializeElement(value, serializer);
        }

        @Override
        public void close() throws IOException {
            writer.close();
            value = holder.getValue();
        }
    }

    private final class MapWriterImpl implements DataMapWriter {
        private final MapValueHolder holder = new MapValueHolder();
        private final DataMapWriter writer;

        @MustBeClosed
        // TODO: Don't suppress MustBeClosedChecker when it supports this flow
        @SuppressWarnings("MustBeClosedChecker")
        MapWriterImpl(int sizeHint) throws IOException {
            writer = holder.serializeMap(sizeHint);
        }

        @Override
        public <K extends @Nullable Object> void serializeEntryKey(K key, ValueSerializer<? super K> serializer) throws IOException {
            writer.serializeEntryKey(key, serializer);
        }

        @Override
        public <V extends @Nullable Object> void serializeEntryValue(V value, ValueSerializer<? super V> serializer) throws IOException {
            writer.serializeEntryValue(value, serializer);
        }

        @Override
        public void close() throws IOException {
            writer.close();
            value = holder.getValue();
        }
    }

    private final class StructWriterImpl implements DataStructWriter {
        private final StructValueHolder holder = new StructValueHolder();
        private final DataStructWriter writer;

        @MustBeClosed
        // TODO: Don't suppress MustBeClosedChecker when it supports this flow
        @SuppressWarnings("MustBeClosedChecker")
        StructWriterImpl(int sizeHint) throws IOException {
            writer = holder.serializeStruct(sizeHint);
        }

        @Override
        public <T extends @Nullable Object> void serializeField(String name, T value, ValueSerializer<? super T> serializer) throws IOException {
            writer.serializeField(name, value, serializer);
        }

        @Override
        public void close() throws IOException {
            writer.close();
            value = holder.getValue();
        }
    }

    private @Nullable Object value;

    @Override
    public void serializeNull() throws IOException {
        beforeSerializeValue();
        assert value == null;
    }

    @Override
    public void serializeBoolean(boolean value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeByte(byte value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeShort(short value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeInt(int value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeLong(long value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeBigInteger(BigInteger value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeFloat(float value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeDouble(double value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeBigDecimal(BigDecimal value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeChar(char value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeString(String value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    public void serializeByteArray(byte[] value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int sizeHint) throws IOException {
        beforeSerializeValue();
        return new SequenceWriterImpl(sizeHint);
    }

    @Override
    @MustBeClosed
    public DataMapWriter serializeMap(int sizeHint) throws IOException {
        beforeSerializeValue();
        return new MapWriterImpl(sizeHint);
    }

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int sizeHint) throws IOException {
        beforeSerializeValue();
        return new StructWriterImpl(sizeHint);
    }

    @Override
    @Nullable Object getValueInternal() {
        return value;
    }
}
