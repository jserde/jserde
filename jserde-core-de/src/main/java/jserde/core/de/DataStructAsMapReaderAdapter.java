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

package jserde.core.de;

import java.io.IOException;
import jserde.core.de.holder.DataValueHolder;
import org.jspecify.annotations.Nullable;

/**
 * Adapter for a {@link DataStructReader} as a {@link DataMapReader}.
 *
 * @author Laurent Pireyn
 * @see DataStructReader#asMapReader()
 */
final class DataStructAsMapReaderAdapter implements DataMapReader {
    private final DataStructReader structReader;

    DataStructAsMapReaderAdapter(DataStructReader structReader) {
        this.structReader = structReader;
    }

    @Override
    public boolean hasNextEntry() throws IOException {
        return structReader.hasNextField();
    }

    @Override
    public <T extends @Nullable Object> T nextEntryKey(ValueDeserializer<T> deserializer) throws IOException {
        return deserializer.deserializeValue(DataValueHolder.on(structReader.nextFieldName()));
    }

    @Override
    public <T extends @Nullable Object> T entryValue(ValueDeserializer<T> deserializer) throws IOException {
        return structReader.fieldValue(deserializer);
    }

    @Override
    public int getSizeHint() {
        return structReader.getSizeHint();
    }
}
