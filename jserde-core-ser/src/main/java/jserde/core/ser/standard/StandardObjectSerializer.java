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

package jserde.core.ser.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import jserde.core.ser.DataValueWriter;
import jserde.core.ser.SerializationException;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueSerializer} that serializes values of standard types to the corresponding data types.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardObjectSerializer implements ValueSerializer<@Nullable Object> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardObjectSerializer INSTANCE = new StandardObjectSerializer();

    private static <T extends @Nullable Object> @Nullable ValueSerializer<? super T> resolveValueSerializer(T value) {
        return value != null
            ? StandardValueSerializerResolver.INSTANCE.resolveValueSerializer((Class<T>) value.getClass())
            : StandardNullSerializer.INSTANCE;
    }

    private static <T extends @Nullable Object> void serializeValueT(T value, DataValueWriter writer) throws IOException {
        final var serializer = resolveValueSerializer(value);
        if (serializer == null) {
            assert value != null;
            throw new SerializationException("The value is of a non-standard type: " + value.getClass().getName());
        }
        serializer.serializeValue(value, writer);
    }

    private StandardObjectSerializer() {}

    @Override
    public void serializeValue(@Nullable Object value, DataValueWriter writer) throws IOException {
        serializeValueT(value, writer);
    }
}
