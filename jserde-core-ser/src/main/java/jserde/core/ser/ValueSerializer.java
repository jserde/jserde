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
 * Value serializer.
 *
 * @author Laurent Pireyn
 * @param <T> the type of values supported by this serializer
 */
@FunctionalInterface
public interface ValueSerializer<T extends @Nullable Object> {
    /**
     * Serializes the given value to the given {@link DataValueWriter}.
     *
     * @param value the value to serialize
     * @param writer the data writer
     * @throws IOException if an I/O error occurs
     */
    void serializeValue(T value, DataValueWriter writer) throws IOException;
}
