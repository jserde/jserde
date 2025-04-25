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
import java.util.ArrayList;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataSequenceReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DeserializationException;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueDeserializer} that produces a {@link List} of {@link Object}s from a {@link DataType#SEQUENCE}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardListDeserializer extends StandardValueDeserializer<List<@Nullable Object>> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.SEQUENCE));
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    /**
     * Singleton instance of this class.
     */
    public static final StandardListDeserializer INSTANCE = new StandardListDeserializer();

    private StandardListDeserializer() {}

    @Override
    public List<@Nullable Object> deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeSequence(this);
    }

    @Override
    public List<@Nullable Object> visitSequence(DataSequenceReader reader) throws IOException {
        final var sizeHint = reader.getSizeHint();
        final List<@Nullable Object> list = new ArrayList<>(sizeHint >= 0 ? sizeHint : DEFAULT_INITIAL_CAPACITY);
        int index = 0;
        while (reader.hasNextElement()) {
            final Object element;
            try {
                element = reader.nextElement(StandardObjectDeserializer.INSTANCE);
            } catch (DeserializationException e) {
                throw new DeserializationException("The element #" + index + " in the sequence cannot be deserialized", e);
            }
            list.add(element);
            ++index;
        }
        return list;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
