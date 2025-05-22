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
import java.util.Collection;
import java.util.Iterator;
import jserde.core.DataType;
import jserde.core.de.DataSequenceReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.core.de.DeserializationException;
import jserde.core.de.ValueDeserializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a fixed {@link DataType#SEQUENCE}.
 *
 * @author Laurent Pireyn
 */
public final class SequenceValueHolder extends DataValueHolder {
    private final class DataSequenceReaderImpl implements DataSequenceReader {
        private final Iterator<? extends @Nullable Object> iterator = value.iterator();
        private int index;

        @Override
        public boolean hasNextElement() {
            return iterator.hasNext();
        }

        @Override
        public <T extends @Nullable Object> T nextElement(ValueDeserializer<T> deserializer) throws IOException {
            final DataValueReader reader;
            try {
                reader = on(iterator.next());
            } catch (IllegalArgumentException e) {
                throw new DeserializationException("Cannot create data value reader for element #" + index + " in collection", e);
            }
            final var value = deserializer.deserializeValue(reader);
            ++index;
            return value;
        }

        @Override
        public int getSizeHint() {
            return value.size();
        }
    }

    private final Collection<? extends @Nullable Object> value;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public SequenceValueHolder(Collection<? extends @Nullable Object> value) {
        this.value = value;
    }

    @Override
    <T> T deserializeValue(DataValueVisitor<T> visitor) throws IOException {
        return visitor.visitSequence(new DataSequenceReaderImpl());
    }
}
