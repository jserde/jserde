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
import java.util.HashMap;
import java.util.Map;
import jserde.core.DataType;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a {@link DataType#STRUCT}.
 *
 * @author Laurent Pireyn
 */
public final class StructValueHolder extends DataValueHolder<Map<String, @Nullable Object>> {
    private final class StructWriterImpl implements DataStructWriter {
        @Override
        public <T extends @Nullable Object> void serializeField(String name, T value, ValueSerializer<? super T> serializer) throws IOException {
            assert map != null;
            final var writer = new ObjectValueHolder();
            try (writer) {
                serializer.serializeValue(value, writer);
            }
            map.put(name, writer.getValue());
        }

        @Override
        public void close() {
            StructValueHolder.this.close();
        }
    }

    private @Nullable Map<String, @Nullable Object> map;

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int sizeHint) throws IOException {
        beforeSerializeValue();
        // TODO: Extract constant for initial capacity
        map = new HashMap<>(sizeHint >= 0 ? sizeHint : 10);
        return new StructWriterImpl();
    }

    @Override
    Map<String, @Nullable Object> getValueInternal() {
        assert map != null;
        return map;
    }
}
