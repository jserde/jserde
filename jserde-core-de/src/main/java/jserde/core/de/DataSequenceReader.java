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
 * Format-specific sequence {@link DataContainerReader}.
 *
 * @author Laurent Pireyn
 */
public non-sealed interface DataSequenceReader extends DataContainerReader {
    /**
     * Returns whether there is a next element in the sequence.
     *
     * @return {@code true} if there is a next element in the sequence,
     * {@code false} if there are no more remaining elements
     * @throws IOException if an I/O error occurs
     */
    boolean hasNextElement() throws IOException;

    /**
     * Deserializes a value from the next element in the sequence with the given {@link ValueDeserializer}.
     *
     * @param deserializer the deserializer
     * @return the value produced by {@code deserializer} from the next element in the sequence
     * @param <T> the type of value produced by {@code deserializer}
     * @throws IOException if an I/O error occurs
     * @throws NoSuchElementException if there are no more remaining elements
     */
    <T extends @Nullable Object> T nextElement(ValueDeserializer<T> deserializer) throws IOException;

    /**
     * Returns the number of elements in the sequence, if known.
     *
     * <p>The default implementation returns -1.
     *
     * @return the number of elements in the sequence,
     * or -1 if it is not known
     */
    default int getSizeHint() {
        return -1;
    }
}
