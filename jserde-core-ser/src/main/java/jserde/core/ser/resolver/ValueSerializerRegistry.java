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

package jserde.core.ser.resolver;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link ValueSerializer} registry.
 *
 * @author Laurent Pireyn
 */
public final class ValueSerializerRegistry implements ValueSerializerResolver {
    private final Map<Type, ValueSerializer<?>> serializersByType = new HashMap<>();

    @Override
    public <T extends @Nullable Object> @Nullable ValueSerializer<? super T> resolveValueSerializer(Class<T> cls) {
        return (ValueSerializer<? super T>) serializersByType.get(cls);
    }

    public <T extends @Nullable Object> void registerValueSerializer(Class<T> cls, ValueSerializer<? super T> serializer) {
        serializersByType.put(cls, serializer);
    }
}
