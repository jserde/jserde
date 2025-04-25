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
import org.jspecify.annotations.Nullable;

/**
 * Value deserializer.
 *
 * @author Laurent Pireyn
 * @param <T> the type of values produced by this deserializer
 * @see DataValueReader
 */
@FunctionalInterface
public interface ValueDeserializer<T extends @Nullable Object> {
    /**
     * Deserializes a value from the given {@link DataValueReader}.
     *
     * <p>Methods of the data value reader take a {@link DataValueVisitor}.
     * The value deserializer may implement that interface and use itself for that role.
     *
     * @param reader the data reader
     * @return the value deserialized from {@code reader}
     * @throws IOException if an I/O error occurs
     */
    T deserializeValue(DataValueReader reader) throws IOException;
}
