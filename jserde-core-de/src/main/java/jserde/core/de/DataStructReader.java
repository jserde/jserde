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
 * Format-specific struct {@link DataContainerReader}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataStructReader extends DataContainerReader {
    /**
     * Returns whether there is a next field in the struct.
     *
     * @return {@code true} if there is a next field in the struct,
     * {@code false} if there are no more remaining fields
     * @throws IOException if an I/O error occurs
     */
    boolean hasNextField() throws IOException;

    /**
     * Returns the name of the next field in the struct.
     *
     * <p>Each invocation of this method must be followed by an invocation of {@link #fieldValue(ValueDeserializer)}.
     *
     * @return the name of the next field in the struct
     * @throws IOException if an I/O error occurs
     * @throws NoSuchElementException if there are no more remaining fields
     * @throws IllegalStateException if this method is invoked twice without invoking {@link #fieldValue(ValueDeserializer)}
     */
    String nextFieldName() throws IOException;

    /**
     * Deserializes a value from the value of the field in the struct with the given {@link ValueDeserializer}.
     *
     * <p>Each invocation of this method must be preceded by an invocation of {@link #nextFieldName()}.
     *
     * @param deserializer the deserializer
     * @return the value produced by {@code deserializer} from the value of the field in the struct
     * @param <T> the type of value produced by {@code deserializer}
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if this method is invoked without first invoking {@link #nextFieldName()}
     */
    <T extends @Nullable Object> T fieldValue(ValueDeserializer<T> deserializer) throws IOException;

    /**
     * Returns the number of fields in the struct, if known.
     *
     * <p>The default implementation returns -1.
     *
     * @return the number of fields in the struct,
     * or -1 if it is not known
     */
    default int getSizeHint() {
        return -1;
    }

    /**
     * Returns this struct reader as a {@link DataMapReader}.
     *
     * <p>The default implementation returns an internal adapter.
     *
     * @return this struct reader as a map reader
     */
    default DataMapReader asMapReader() {
        return new DataStructAsMapReaderAdapter(this);
    }
}
