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

import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * Chain {@link ValueSerializerResolver}.
 *
 * @author Laurent Pireyn
 */
public final class ChainValueSerializerResolver implements ValueSerializerResolver {
    private final Iterable<? extends ValueSerializerResolver> resolvers;

    public ChainValueSerializerResolver(Iterable<? extends ValueSerializerResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public <T extends @Nullable Object> @Nullable ValueSerializer<? super T> resolveValueSerializer(Class<T> cls) {
        for (final var resolver : resolvers) {
            final var serializer = resolver.resolveValueSerializer(cls);
            if (serializer != null) {
                return serializer;
            }
        }
        return null;
    }
}
