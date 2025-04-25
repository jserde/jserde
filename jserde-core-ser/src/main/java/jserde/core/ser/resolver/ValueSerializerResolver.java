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
 * {@link ValueSerializer} resolver.
 *
 * @author Laurent Pireyn
 */
@FunctionalInterface
public interface ValueSerializerResolver {
    /**
     * Resolves a {@link ValueSerializer} for the given class.
     *
     * @param cls the class
     * @param <T> the type of {@code cls}
     * @return a value serializer for {@code cls},
     * or {@code null} if no value serializers can be resolved
     */
    <T extends @Nullable Object> @Nullable ValueSerializer<? super T> resolveValueSerializer(Class<T> cls);
}
