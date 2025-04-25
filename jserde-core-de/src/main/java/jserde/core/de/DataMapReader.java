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
import java.util.NoSuchElementException;
import org.jspecify.annotations.Nullable;

/**
 * Format-specific map {@link DataContainerReader}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataMapReader extends DataContainerReader {
    /**
     * Returns whether there is a next entry in the map.
     *
     * @return {@code true} if there is a next entry in the map,
     * {@code false} if there are no more remaining entries
     * @throws IOException if an I/O error occurs
     */
    boolean hasNextEntry() throws IOException;

    /**
     * Deserializes a value from the key of the next entry in the map with the given {@link ValueDeserializer}.
     *
     * <p>Each invocation of this method must be followed by an invocation of {@link #entryValue(ValueDeserializer)}.
     *
     * @param deserializer the deserializer
     * @return the value produced by {@code deserializer} from the key of the next entry in the map
     * @param <T> the type of value produced by {@code deserializer}
     * @throws IOException if an I/O error occurs
     * @throws NoSuchElementException if there are no more remaining entries
     * @throws IllegalStateException if this method is invoked twice without invoking {@link #entryValue(ValueDeserializer)}
     */
    <T extends @Nullable Object> T nextEntryKey(ValueDeserializer<T> deserializer) throws IOException;

    /**
     * Deserializes a value from the value of the entry in the map with the given {@link ValueDeserializer}.
     *
     * <p>Each invocation of this method must be preceded by an invocation of {@link #nextEntryKey(ValueDeserializer)}.
     *
     * @param deserializer the deserializer
     * @return the value produced by {@code deserializer} from the value of the entry in the map
     * @param <T> the type of value produced by {@code deserializer}
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if this method is invoked without first invoking {@link #nextEntryKey(ValueDeserializer)}
     */
    <T extends @Nullable Object> T entryValue(ValueDeserializer<T> deserializer) throws IOException;

    /**
     * Returns the number of entries remaining in the map, if known.
     *
     * <p>The default implementation returns -1.
     *
     * @return the number of entries remaining in the map,
     * or -1 if it is not known
     */
    default int getSizeHint() {
        return -1;
    }
}
