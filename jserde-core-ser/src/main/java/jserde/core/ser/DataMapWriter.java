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

import java.io.IOException;
import org.jspecify.annotations.Nullable;

/**
 * Format-specific map {@link DataWriter}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataMapWriter extends DataContainerWriter {
    /**
     * Serializes the key of an entry in the map using the given {@link ValueSerializer}.
     *
     * <p>Each invocation of this method must be followed by an invocation of {@link #serializeEntryValue(Object, ValueSerializer)}.
     *
     * @param key the key
     * @param serializer the serializer for {@code key}
     * @param <K> the type of {@code key}
     * @throws IOException if an I/O error occurs
     */
    <K extends @Nullable Object> void serializeEntryKey(K key, ValueSerializer<? super K> serializer) throws IOException;

    /**
     * Serializes the value of the entry in the map using the given {@link ValueSerializer}.
     *
     * <p>Each invocation of this method must be preceded by an invocation of {@link #serializeEntryKey(Object, ValueSerializer)}.
     *
     * @param value the value
     * @param serializer the serializer for {@code value}
     * @param <V> the type of {@code value}
     * @throws IOException if an I/O error occurs
     */
    <V extends @Nullable Object> void serializeEntryValue(V value, ValueSerializer<? super V> serializer) throws IOException;
}
