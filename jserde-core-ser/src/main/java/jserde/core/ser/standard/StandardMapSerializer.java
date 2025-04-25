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

package jserde.core.ser.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.util.Map;
import jserde.core.DataType;
import jserde.core.ser.DataValueWriter;
import jserde.core.ser.SerializationException;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueSerializer} that serializes a {@link Map} to a {@link DataType#MAP}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardMapSerializer extends StandardValueSerializer<Map<? extends @Nullable Object, ? extends @Nullable Object>> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardMapSerializer INSTANCE = new StandardMapSerializer();

    private StandardMapSerializer() {}

    @Override
    public void serializeValue(Map<? extends @Nullable Object, ? extends @Nullable Object> value, DataValueWriter writer) throws IOException {
        try (var mapWriter = writer.serializeMap(value.size())) {
            int index = 0;
            for (final var entry : value.entrySet()) {
                final Object key = entry.getKey();
                try {
                    mapWriter.serializeEntryKey(key, StandardObjectSerializer.INSTANCE);
                } catch (SerializationException e) {
                    throw new SerializationException("The key of entry #" + index + " in the map cannot be serialized", e);
                }
                try {
                    mapWriter.serializeEntryValue(entry.getValue(), StandardObjectSerializer.INSTANCE);
                } catch (SerializationException e) {
                    throw new SerializationException("The value of entry with key " + key + " (#" + index + ") in the map cannot be serialized", e);
                }
                ++index;
            }
        }
    }
}
