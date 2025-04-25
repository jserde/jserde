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
 * {@link StandardValueSerializer} that serializes a {@link Map} with {@link String}s as keys to a {@link DataType#STRUCT}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardStructSerializer extends StandardValueSerializer<Map<String, ? extends @Nullable Object>> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardStructSerializer INSTANCE = new StandardStructSerializer();

    private StandardStructSerializer() {}

    @Override
    public void serializeValue(Map<String, ? extends @Nullable Object> value, DataValueWriter writer) throws IOException {
        try (var structWriter = writer.serializeStruct(value.size())) {
            int index = 0;
            for (final var entry : value.entrySet()) {
                final String key = entry.getKey();
                try {
                    structWriter.serializeField(key, entry.getValue(), StandardObjectSerializer.INSTANCE);
                } catch (SerializationException e) {
                    throw new SerializationException("The value of field \"" + key + "\" (#" + index + ") in the map cannot be serialized", e);
                }
                ++index;
            }
        }
    }
}
