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

import com.google.errorprone.annotations.ForOverride;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import jserde.core.DataType;
import jserde.core.de.DataMapReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.core.de.DeserializationException;
import jserde.core.de.ValueDeserializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a fixed {@link DataType#MAP}.
 *
 * @author Laurent Pireyn
 */
public non-sealed class MapValueHolder extends DataValueHolder {
    private final class DataMapReaderImpl implements DataMapReader {
        private final Iterator<? extends Entry<? extends @Nullable Object, ? extends @Nullable Object>> iterator = value.entrySet().iterator();
        private @Nullable Entry<? extends @Nullable Object, ? extends @Nullable Object> nextEntry = computeNextEntry();
        private int index;

        private @Nullable Entry<? extends @Nullable Object, ? extends @Nullable Object> computeNextEntry() {
            return iterator.hasNext() ? iterator.next() : null;
        }

        @Override
        public boolean hasNextEntry() {
            return nextEntry != null;
        }

        @Override
        public <T extends @Nullable Object> T nextEntryKey(ValueDeserializer<T> deserializer) throws IOException {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            // FIXME: Throw IllegalStateException if this is invoked twice
            final DataValueReader reader;
            try {
                reader = createEntryKeyReader(nextEntry.getKey());
            } catch (IllegalArgumentException e) {
                throw new DeserializationException("Cannot create data value reader for key of entry #" + index + " in map", e);
            }
            return deserializer.deserializeValue(reader);
        }

        @Override
        public <T extends @Nullable Object> T entryValue(ValueDeserializer<T> deserializer) throws IOException {
            if (nextEntry == null) {
                throw new IllegalStateException();
            }
            // FIXME: Throw IllegalStateException if this is invoked twice
            final DataValueReader reader;
            try {
                reader = createEntryValueReader(nextEntry.getValue());
            } catch (IllegalArgumentException e) {
                throw new DeserializationException("Cannot create data value reader for value of entry with key " + nextEntry.getKey() + " (#" + index + ") in map", e);
            }
            final var value = deserializer.deserializeValue(reader);
            ++index;
            nextEntry = computeNextEntry();
            return value;
        }

        @Override
        public int getSizeHint() {
            return value.size();
        }
    }

    private final Map<? extends @Nullable Object, ? extends @Nullable Object> value;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public MapValueHolder(Map<? extends @Nullable Object, ? extends @Nullable Object> value) {
        this.value = value;
    }

    @Override
    final <T extends @Nullable Object> T deserializeValue(DataValueVisitor<T> visitor) throws IOException {
        return visitor.visitMap(new DataMapReaderImpl());
    }

    /**
     * Creates a {@link DataValueReader} for the given entry key.
     *
     * <p>This implementation invokes {@link DataValueHolder#on(Object)}.
     *
     * @param key the entry key
     * @return a value reader for {@code key}
     * @throws IllegalArgumentException if no value reader can be created for {@code key}
     */
    @ForOverride
    protected DataValueReader createEntryKeyReader(@Nullable Object key) {
        return on(key);
    }

    /**
     * Creates a {@link DataValueReader} for the given entry value.
     *
     * <p>This implementation invokes {@link DataValueHolder#on(Object)}.
     *
     * @param value the entry value
     * @return a value reader for {@code value}
     * @throws IllegalArgumentException if no value reader can be created for {@code value}
     */
    @ForOverride
    protected DataValueReader createEntryValueReader(@Nullable Object value) {
        return on(value);
    }
}
