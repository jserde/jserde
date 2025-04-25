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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import jserde.core.ser.ValueSerializer;
import jserde.core.ser.resolver.ValueSerializerResolver;
import org.jspecify.annotations.Nullable;

/**
 * {@link ValueSerializerResolver} for {@link StandardValueSerializer}s.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardValueSerializerResolver implements ValueSerializerResolver {
    /**
     * Singleton instance of this class.
     */
    public static final StandardValueSerializerResolver INSTANCE = new StandardValueSerializerResolver();

    private StandardValueSerializerResolver() {}

    @Override
    public @Nullable <T> ValueSerializer<? super T> resolveValueSerializer(Class<T> cls) {
        ValueSerializer<?> serializer = null;
        if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)) {
            serializer = StandardBooleanSerializer.INSTANCE;
        } else if (cls.equals(Byte.class) || cls.equals(Byte.TYPE)) {
            serializer = StandardByteSerializer.INSTANCE;
        } else if (cls.equals(Short.class) || cls.equals(Short.TYPE)) {
            serializer = StandardShortSerializer.INSTANCE;
        } else if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)) {
            serializer = StandardIntSerializer.INSTANCE;
        } else if (cls.equals(Long.class) || cls.equals(Long.TYPE)) {
            serializer = StandardLongSerializer.INSTANCE;
        } else if (cls.equals(BigInteger.class)) {
            serializer = StandardBigIntegerSerializer.INSTANCE;
        } else if (cls.equals(Float.class) || cls.equals(Float.TYPE)) {
            serializer = StandardFloatSerializer.INSTANCE;
        } else if (cls.equals(Double.class) || cls.equals(Double.TYPE)) {
            serializer = StandardDoubleSerializer.INSTANCE;
        } else if (cls.equals(BigDecimal.class)) {
            serializer = StandardBigDecimalSerializer.INSTANCE;
        } else if (cls.equals(Character.class) || cls.equals(Character.TYPE)) {
            serializer = StandardCharSerializer.INSTANCE;
        } else if (cls.equals(String.class)) {
            serializer = StandardStringSerializer.INSTANCE;
        } else if (cls.equals(byte[].class)) {
            serializer = StandardByteArraySerializer.INSTANCE;
        } else if (Collection.class.isAssignableFrom(cls)) {
            serializer = StandardCollectionSerializer.INSTANCE;
        } else if (Map.class.isAssignableFrom(cls)) {
            serializer = StandardMapSerializer.INSTANCE;
        }
        return (ValueSerializer<? super T>) serializer;
    }
}
