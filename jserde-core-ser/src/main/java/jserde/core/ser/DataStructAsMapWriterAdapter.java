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

package jserde.core.ser;

import com.google.errorprone.annotations.MustBeClosed;
import java.io.IOException;
import jserde.core.ser.holder.StringValueHolder;
import org.jspecify.annotations.Nullable;

/**
 * Adapter for a {@link DataStructWriter} as a {@link DataStructWriter}.
 *
 * @author Laurent Pireyn
 * @see DataStructWriter#asMapWriter()
 */
final class DataStructAsMapWriterAdapter implements DataMapWriter {
    private final DataStructWriter structWriter;
    private @Nullable String fieldName;

    @MustBeClosed
    DataStructAsMapWriterAdapter(DataStructWriter structWriter) {
        this.structWriter = structWriter;
    }

    @Override
    public <K extends @Nullable Object> void serializeEntryKey(K key, ValueSerializer<? super K> serializer) throws IOException {
        if (fieldName != null) {
            throw new IllegalStateException("The previous entry value has not been serialized"
                + " (hint: invoke serializeEntryValue(...) after each invocation of serializeEntryKey(...))"
            );
        }
        final var writer = new StringValueHolder();
        try (writer) {
            serializer.serializeValue(key, writer);
        }
        fieldName = writer.getValue();
    }

    @Override
    public <V extends @Nullable Object> void serializeEntryValue(V value, ValueSerializer<? super V> serializer) throws IOException {
        if (fieldName == null) {
            throw new IllegalStateException("The entry key has not been serialized"
                + " (hint: invoke serializeEntryKey(...) before each invocation of serializeEntryValue(...))"
            );
        }
        try {
            structWriter.serializeField(fieldName, value, serializer);
        } finally {
            fieldName = null;
        }
    }

    @Override
    public void close() throws IOException {
        structWriter.close();
    }
}
