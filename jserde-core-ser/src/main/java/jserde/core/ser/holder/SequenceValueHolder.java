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
import java.util.ArrayList;
import java.util.List;
import jserde.core.DataType;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a {@link DataType#SEQUENCE}.
 *
 * @author Laurent Pireyn
 */
public final class SequenceValueHolder extends DataValueHolder<List<@Nullable Object>> {
    private final class SequenceWriterImpl implements DataSequenceWriter {
        @Override
        public <T extends @Nullable Object> void serializeElement(T value, ValueSerializer<? super T> serializer) throws IOException {
            assert list != null;
            final var writer = new ObjectValueHolder();
            try (writer) {
                serializer.serializeValue(value, writer);
            }
            list.add(writer.getValue());
        }

        @Override
        public void close() {
            SequenceValueHolder.this.close();
        }
    }

    private @Nullable List<@Nullable Object> list;

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int sizeHint) throws IOException {
        beforeSerializeValue();
        // TODO: Extract constant for initial capacity
        list = new ArrayList<>(sizeHint >= 0 ? sizeHint : 10);
        return new SequenceWriterImpl();
    }

    @Override
    List<@Nullable Object> getValueInternal() {
        assert list != null;
        return list;
    }
}
