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

import jserde.core.ser.ValueSerializer;
import org.jspecify.annotations.Nullable;

/**
 * Standard {@link ValueSerializer}.
 *
 * @author Laurent Pireyn
 * @param <T> the type of value supported by this serializer
 */
public abstract sealed class StandardValueSerializer<T extends @Nullable Object> implements ValueSerializer<T> permits StandardNullSerializer, StandardBooleanSerializer, StandardByteSerializer, StandardShortSerializer, StandardIntSerializer, StandardLongSerializer, StandardBigIntegerSerializer, StandardFloatSerializer, StandardDoubleSerializer, StandardBigDecimalSerializer, StandardCharSerializer, StandardStringSerializer, StandardByteArraySerializer, StandardCollectionSerializer, StandardMapSerializer, StandardStructSerializer {
    @Override
    public final boolean equals(@Nullable Object object) {
        // All subclasses are singletons, so equality is identity
        return this == object;
    }

    @Override
    public final int hashCode() {
        // All subclasses are singletons, so the hash code is the identity hash code
        return System.identityHashCode(this);
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}
