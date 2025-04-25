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
 * {@link ValueDeserializerResolver} wrapper that adds caching.
 *
 * @author Laurent Pireyn
 */
public final class CachingValueDeserializerResolver implements ValueDeserializerResolver {
    private final ValueDeserializerResolver resolver;
    private final Map<Type, ValueDeserializer<?>> cache = new HashMap<>();

    public CachingValueDeserializerResolver(ValueDeserializerResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public <T extends @Nullable Object> @Nullable ValueDeserializer<? extends T> resolveValueDeserializer(Class<T> cls) {
        return (ValueDeserializer<? extends T>) cache.computeIfAbsent(cls, key -> resolver.resolveValueDeserializer(cls));
    }
}
