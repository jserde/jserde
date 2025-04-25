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

package jserde.core.de.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataMapReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DeserializationException;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueDeserializer} that produces a {@link Map} of {@link Object}s by {@code Object} from a {@link DataType#MAP}.
 *
 * <p>This deserializer does not conserve the order of the entries.
 * In case of duplicate entries, the last one wins.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardMapDeserializer extends StandardValueDeserializer<Map<@Nullable Object, @Nullable Object>> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.MAP));
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    /**
     * Singleton instance of this class.
     */
    public static final StandardMapDeserializer INSTANCE = new StandardMapDeserializer();

    private StandardMapDeserializer() {}

    @Override
    public Map<@Nullable Object, @Nullable Object> deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeMap(this);
    }

    @Override
    public Map<@Nullable Object, @Nullable Object> visitMap(DataMapReader reader) throws IOException {
        final var sizeHint = reader.getSizeHint();
        final Map<@Nullable Object, @Nullable Object> map = new HashMap<>(sizeHint >= 0 ? sizeHint : DEFAULT_INITIAL_CAPACITY);
        int index = 0;
        while (reader.hasNextEntry()) {
            final Object key;
            try {
                key = reader.nextEntryKey(StandardObjectDeserializer.INSTANCE);
            } catch (DeserializationException e) {
                throw new DeserializationException("The key of entry #" + index + " in the map cannot be deserialized", e);
            }
            final Object value;
            try {
                value = reader.entryValue(StandardObjectDeserializer.INSTANCE);
            } catch (DeserializationException e) {
                throw new DeserializationException("The value of entry with key " + key + " (#" + index + ") in the map cannot be deserialized", e);
            }
            map.put(key, value);
            ++index;
        }
        return map;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
