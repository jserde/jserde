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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import jserde.core.de.holder.DataValueHolder;
import jserde.test.AbstractTests;
import org.jspecify.annotations.Nullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StandardDeserializersTests extends AbstractTests {
    @ParameterizedTest
    @MethodSource
    <T extends @Nullable Object> void testStandardValueDeserializer(StandardValueDeserializer<T> deserializer, @Nullable Object value) throws IOException {
        final var actualValue = deserializer.deserializeValue(DataValueHolder.on(value));
        log("Deserialized value: " + actualValue);
        if (value != null) {
            assertNotNull(actualValue);
            if (Number.class.isAssignableFrom(value.getClass())) {
                // We cannot use equality for numbers of different classes, so we compare their string representation
                assertEquals(value.toString(), actualValue.toString());
            } else {
                assertEquals(value, actualValue);
            }
        } else {
            assertNull(actualValue);
        }
    }

    static Iterable<Arguments> testStandardValueDeserializer() {
        return List.of(
            // Null
            arguments(StandardNullDeserializer.INSTANCE, null),
            // Boolean
            arguments(StandardBooleanDeserializer.INSTANCE, true),
            // Byte
            arguments(StandardByteDeserializer.INSTANCE, (byte) 123),
            // Short
            arguments(StandardShortDeserializer.INSTANCE, (short) 123),
            arguments(StandardShortDeserializer.INSTANCE, (byte) 123),
            // Int
            arguments(StandardIntDeserializer.INSTANCE, 123),
            arguments(StandardIntDeserializer.INSTANCE, (short) 123),
            arguments(StandardIntDeserializer.INSTANCE, (byte) 123),
            // Long
            arguments(StandardLongDeserializer.INSTANCE, 123L),
            arguments(StandardLongDeserializer.INSTANCE, 123),
            arguments(StandardLongDeserializer.INSTANCE, (short) 123),
            arguments(StandardLongDeserializer.INSTANCE, (byte) 123),
            // Big integer
            arguments(StandardBigIntegerDeserializer.INSTANCE, BigInteger.valueOf(123)),
            arguments(StandardBigIntegerDeserializer.INSTANCE, 123L),
            arguments(StandardBigIntegerDeserializer.INSTANCE, 123),
            arguments(StandardBigIntegerDeserializer.INSTANCE, (short) 123),
            arguments(StandardBigIntegerDeserializer.INSTANCE, (byte) 123),
            // Float
            arguments(StandardFloatDeserializer.INSTANCE, 1.5f),
            // Double
            arguments(StandardDoubleDeserializer.INSTANCE, 1.5),
            arguments(StandardDoubleDeserializer.INSTANCE, 1.5f),
            // Big decimal
            arguments(StandardBigDecimalDeserializer.INSTANCE, BigDecimal.valueOf(1.5)),
            arguments(StandardBigDecimalDeserializer.INSTANCE, 1.5),
            arguments(StandardBigDecimalDeserializer.INSTANCE, 1.5f),
            arguments(StandardBigDecimalDeserializer.INSTANCE, BigInteger.valueOf(123)),
            arguments(StandardBigDecimalDeserializer.INSTANCE, 123L),
            arguments(StandardBigDecimalDeserializer.INSTANCE, 123),
            arguments(StandardBigDecimalDeserializer.INSTANCE, (short) 123),
            arguments(StandardBigDecimalDeserializer.INSTANCE, (byte) 123),
            // Char
            arguments(StandardCharDeserializer.INSTANCE, 'a'),
            // String
            arguments(StandardStringDeserializer.INSTANCE, "abc"),
            // Local date
            arguments(StandardLocalDateDeserializer.INSTANCE, LocalDate.of(2025, 5, 1)),
            // Local time
            arguments(StandardLocalTimeDeserializer.INSTANCE, LocalTime.of(17, 49, 13)),
            // Local date-time
            arguments(StandardLocalDateTimeDeserializer.INSTANCE, LocalDateTime.of(2025, 5, 1, 17, 49, 13)),
            // Offset date-time
            arguments(StandardOffsetDateTimeDeserializer.INSTANCE, OffsetDateTime.of(2025, 5, 1, 17, 49, 13, 0, ZoneOffset.ofHours(2)))
        );
    }
}
