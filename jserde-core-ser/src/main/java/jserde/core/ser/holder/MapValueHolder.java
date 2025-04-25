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
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a {@link DataType#MAP}.
 *
 * @author Laurent Pireyn
 */
public final class MapValueHolder extends DataValueHolder<Map<@Nullable Object, @Nullable Object>> {
    private final class MapWriterImpl implements DataMapWriter {
        private @Nullable Object key;

        @Override
        public <K extends @Nullable Object> void serializeEntryKey(K key, ValueSerializer<? super K> serializer) throws IOException {
            assert map != null;
            // FIXME: Throw IllegalStateException if this method is invoked twice
            final var writer = new ObjectValueHolder();
            try (writer) {
                serializer.serializeValue(key, writer);
            }
            this.key = writer.getValue();
        }

        @Override
        public <V extends @Nullable Object> void serializeEntryValue(V value, ValueSerializer<? super V> serializer) throws IOException {
            assert map != null;
            // FIXME: Throw IllegalStateException if this method is invoked twice
            final var writer = new ObjectValueHolder();
            try (writer) {
                serializer.serializeValue(value, writer);
            }
            map.put(key, writer.getValue());
        }

        @Override
        public void close() {
            MapValueHolder.this.close();
        }
    }

    private @Nullable Map<@Nullable Object, @Nullable Object> map;

    @Override
    @MustBeClosed
    public DataMapWriter serializeMap(int sizeHint) throws IOException {
        beforeSerializeValue();
        // TODO: Extract constant for initial capacity
        map = new HashMap<>(sizeHint >= 0 ? sizeHint : 10);
        return new MapWriterImpl();
    }

    @Override
    Map<@Nullable Object, @Nullable Object> getValueInternal() {
        assert map != null;
        return map;
    }
}
