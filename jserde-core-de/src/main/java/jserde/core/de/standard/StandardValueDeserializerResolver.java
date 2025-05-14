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

package jserde.core.de.standard;

import com.google.errorprone.annotations.Immutable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import jserde.core.de.ValueDeserializer;
import jserde.core.de.resolver.ValueDeserializerResolver;
import org.jspecify.annotations.Nullable;

/**
 * {@link ValueDeserializerResolver} for {@link StandardValueDeserializer}s.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardValueDeserializerResolver implements ValueDeserializerResolver {
    /**
     * Singleton instance of this class.
     */
    public static final StandardValueDeserializerResolver INSTANCE = new StandardValueDeserializerResolver();

    private StandardValueDeserializerResolver() {}

    @Override
    public @Nullable <T> ValueDeserializer<? extends T> resolveValueDeserializer(Class<T> cls) {
        ValueDeserializer<?> deserializer = null;
        if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)) {
            deserializer = StandardBooleanDeserializer.INSTANCE;
        } else if (cls.equals(Byte.class) || cls.equals(Byte.TYPE)) {
            deserializer = StandardByteDeserializer.INSTANCE;
        } else if (cls.equals(Short.class) || cls.equals(Short.TYPE)) {
            deserializer = StandardShortDeserializer.INSTANCE;
        } else if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)) {
            deserializer = StandardIntDeserializer.INSTANCE;
        } else if (cls.equals(Long.class) || cls.equals(Long.TYPE)) {
            deserializer = StandardLongDeserializer.INSTANCE;
        } else if (cls.equals(BigInteger.class)) {
            deserializer = StandardBigIntegerDeserializer.INSTANCE;
        } else if (cls.equals(Float.class) || cls.equals(Float.TYPE)) {
            deserializer = StandardFloatDeserializer.INSTANCE;
        } else if (cls.equals(Double.class) || cls.equals(Double.TYPE)) {
            deserializer = StandardDoubleDeserializer.INSTANCE;
        } else if (cls.equals(BigDecimal.class)) {
            deserializer = StandardBigDecimalDeserializer.INSTANCE;
        } else if (cls.equals(Character.class) || cls.equals(Character.TYPE)) {
            deserializer = StandardCharDeserializer.INSTANCE;
        } else if (cls.equals(String.class)) {
            deserializer = StandardStringDeserializer.INSTANCE;
        } else if (cls.equals(byte[].class)) {
            deserializer = StandardByteArrayDeserializer.INSTANCE;
        } else if (cls.equals(LocalDate.class)) {
            deserializer = StandardLocalDateDeserializer.INSTANCE;
        } else if (cls.equals(LocalTime.class)) {
            deserializer = StandardLocalTimeDeserializer.INSTANCE;
        } else if (cls.equals(LocalDateTime.class)) {
            deserializer = StandardLocalDateTimeDeserializer.INSTANCE;
        } else if (cls.equals(OffsetDateTime.class)) {
            deserializer = StandardOffsetDateTimeDeserializer.INSTANCE;
        } else if (List.class.isAssignableFrom(cls)) {
            deserializer = StandardListDeserializer.INSTANCE;
        } else if (Map.class.isAssignableFrom(cls)) {
            deserializer = StandardMapDeserializer.INSTANCE;
        }
        return (ValueDeserializer<? extends T>) deserializer;
    }
}
