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
import jserde.core.de.DataStructReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DeserializationException;
import jserde.core.de.InvalidValueException;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueDeserializer} that produces a {@link Map} of {@link Object}s by {@link String} from a {@link DataType#STRUCT}.
 *
 * <p>This deserializer does not conserve the order of the fields.
 * Duplicate fields cause an {@link InvalidValueException}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardStructDeserializer extends StandardValueDeserializer<Map<String, @Nullable Object>> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.STRUCT));
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    /**
     * Singleton instance of this class.
     */
    public static final StandardStructDeserializer INSTANCE = new StandardStructDeserializer();

    private StandardStructDeserializer() {}

    @Override
    public Map<String, @Nullable Object> deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeStruct(this);
    }

    @Override
    public Map<String, @Nullable Object> visitStruct(DataStructReader reader) throws IOException {
        final var sizeHint = reader.getSizeHint();
        final Map<String, @Nullable Object> map = new HashMap<>(sizeHint >= 0 ? sizeHint : DEFAULT_INITIAL_CAPACITY);
        int index = 0;
        while (reader.hasNextField()) {
            final String fieldName;
            try {
                fieldName = reader.nextFieldName();
            } catch (DeserializationException e) {
                throw new DeserializationException("The name of field #" + index + " in the struct cannot be read", e);
            }
            if (map.containsKey(fieldName)) {
                throw new InvalidValueException("Duplicate field \"" + fieldName + "\" in the struct");
            }
            final Object fieldValue;
            try {
                fieldValue = reader.fieldValue(StandardObjectDeserializer.INSTANCE);
            } catch (DeserializationException e) {
                throw new DeserializationException("The value of field \"" + fieldName + "\" (#" + index + ") in the struct cannot be deserialized", e);
            }
            map.put(fieldName, fieldValue);
            ++index;
        }
        return map;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
