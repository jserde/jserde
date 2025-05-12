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

package jserde.core.de.holder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import jserde.core.DataType;
import jserde.core.de.DataStructReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.core.de.DeserializationException;
import jserde.core.de.ValueDeserializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a fixed {@link DataType#STRUCT}.
 *
 * @author Laurent Pireyn
 */
public final class StructValueHolder extends DataValueHolder {
    private final class DataStructReaderImpl implements DataStructReader {
        private final Iterator<? extends Entry<String, ? extends @Nullable Object>> iterator = value.entrySet().iterator();
        private @Nullable Entry<String, ? extends @Nullable Object> nextEntry = computeNextEntry();
        private int index;

        private @Nullable Entry<String, ? extends @Nullable Object> computeNextEntry() {
            return iterator.hasNext() ? iterator.next() : null;
        }

        @Override
        public boolean hasNextField() {
            return nextEntry != null;
        }

        @Override
        public String nextFieldName() {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            // FIXME: Throw IllegalStateException if this is invoked twice
            return nextEntry.getKey();
        }

        @Override
        public <T extends @Nullable Object> T fieldValue(ValueDeserializer<T> deserializer) throws IOException {
            if (nextEntry == null) {
                throw new IllegalStateException();
            }
            // FIXME: Throw IllegalStateException if this is invoked twice
            final DataValueReader reader;
            try {
                reader = on(nextEntry.getValue());
            } catch (IllegalArgumentException e) {
                throw new DeserializationException("Cannot create data value reader for value of field \"" + nextEntry.getKey() + "\" (#" + index + ") in struct", e);
            }
            final var value = deserializer.deserializeValue(reader);
            ++index;
            nextEntry = computeNextEntry();
            return value;
        }

        @Override
        public int getSizeHint() {
            return value.size() - index;
        }
    }

    private final Map<String, ? extends @Nullable Object> value;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public StructValueHolder(Map<String, ? extends @Nullable Object> value) {
        this.value = value;
    }

    @Override
    <T extends @Nullable Object> T deserializeValue(DataValueVisitor<T> visitor) throws IOException {
        return visitor.visitStruct(new DataStructReaderImpl());
    }
}
