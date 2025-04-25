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

package jserde.core.de.resolver;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import jserde.core.de.ValueDeserializer;
import org.jspecify.annotations.Nullable;

/**
 * {@link ValueDeserializer} registry.
 *
 * @author Laurent Pireyn
 */
public final class ValueDeserializerRegistry implements ValueDeserializerResolver {
    private final Map<Type, ValueDeserializer<?>> deserializersByType = new HashMap<>();

    @Override
    public <T extends @Nullable Object> @Nullable ValueDeserializer<? extends T> resolveValueDeserializer(Class<T> cls) {
        return (ValueDeserializer<? extends T>) deserializersByType.get(cls);
    }

    public <T extends @Nullable Object> void registerValueDeserializer(Class<T> cls, ValueDeserializer<? extends T> deserializer) {
        deserializersByType.put(cls, deserializer);
    }
}
