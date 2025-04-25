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
import org.jspecify.annotations.Nullable;

/**
 * Format-specific struct {@link DataWriter}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataStructWriter extends DataContainerWriter {
    /**
     * Serializes a field using the given {@link ValueSerializer}.
     *
     * @param name the name of the field
     * @param value the value of the field
     * @param serializer the serializer for {@code value}
     * @param <T> the type of {@code value}
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> void serializeField(String name, T value, ValueSerializer<? super T> serializer) throws IOException;

    /**
     * Returns this struct writer as a {@link DataMapWriter}.
     *
     * <p>The default implementation returns an internal adapter.
     *
     * @return this struct writer as a map writer
     */
    @MustBeClosed
    default DataMapWriter asMapWriter() {
        return new DataStructAsMapWriterAdapter(this);
    }
}
