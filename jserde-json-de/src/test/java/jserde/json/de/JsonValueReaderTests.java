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

package jserde.json.de;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jserde.core.de.ValueDeserializer;
import jserde.core.de.standard.StandardBigDecimalDeserializer;
import jserde.core.de.standard.StandardBigIntegerDeserializer;
import jserde.core.de.standard.StandardBooleanDeserializer;
import jserde.core.de.standard.StandardByteDeserializer;
import jserde.core.de.standard.StandardCharDeserializer;
import jserde.core.de.standard.StandardIntDeserializer;
import jserde.core.de.standard.StandardListDeserializer;
import jserde.core.de.standard.StandardLongDeserializer;
import jserde.core.de.standard.StandardMapDeserializer;
import jserde.core.de.standard.StandardNullDeserializer;
import jserde.core.de.standard.StandardShortDeserializer;
import jserde.core.de.standard.StandardStringDeserializer;
import jserde.test.AbstractTests;
import org.jspecify.annotations.Nullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonValueReaderTests extends AbstractTests {
    static String quoted(String string) {
        return '"' + string + '"';
    }

    <T extends @Nullable Object> T deserializeValue(String input, ValueDeserializer<T> deserializer) throws IOException {
        log("Input: " + input);
        final var value = deserializer.deserializeValue(new JsonValueReader(input));
        // TODO #test #improvement: Invoke Arrays.toString for arrays
        log("Deserialized value: " + value);
        return value;
    }

    <T extends @Nullable Object> void assertDeserializeValue(T expected, String input, ValueDeserializer<? extends T> deserializer) throws IOException {
        assertEquals(expected, deserializeValue(input, deserializer));
    }

    void assertDeserializeValueInvalid(String input, ValueDeserializer<?> deserializer) {
        final var exception = assertThrows(IOException.class, () -> deserializeValue(input, deserializer));
        logExpectedException(exception);
    }

    @Test
    void testDeserializeNull() throws IOException {
        assertDeserializeValue(null, "null", StandardNullDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Invalid syntax
        "NULL",
        "Null",
        "nulL",
        "nul",
        // Other JSON value types
        "true",
        "false",
        "0",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testSkipNullInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardNullDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(booleans = {
        true,
        false,
    })
    void testDeserializeBoolean(boolean value) throws IOException {
        assertDeserializeValue(value, String.valueOf(value), StandardBooleanDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Invalid syntax
        "TRUE",
        "FALSE",
        "True",
        "False",
        "truE",
        "falsE",
        "tru",
        "fals",
        // Other JSON value types
        "null",
        "0",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeBooleanInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardBooleanDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(bytes = {
        0,
        1,
        -1,
        Byte.MIN_VALUE,
        Byte.MAX_VALUE,
    })
    void testDeserializeByte(byte value) throws IOException {
        assertDeserializeValue(value, String.valueOf(value), StandardByteDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0",
        "+1",
        // Out of range
        "128",
        "-129",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeByteInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardByteDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(shorts = {
        0,
        1,
        -1,
        Short.MIN_VALUE,
        Short.MAX_VALUE,
    })
    void testDeserializeShort(short value) throws IOException {
        assertDeserializeValue(value, String.valueOf(value), StandardShortDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0",
        // Out of range
        "32768",
        "-32769",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeShortInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardShortDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(ints = {
        0,
        1,
        -1,
        Integer.MIN_VALUE,
        Integer.MAX_VALUE,
    })
    void testDeserializeInt(int value) throws IOException {
        assertDeserializeValue(value, String.valueOf(value), StandardIntDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0",
        "+1",
        // Out of range
        "2147483648",
        "-2147483649",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeIntInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardIntDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(longs = {
        0L,
        1L,
        -1L,
        Long.MIN_VALUE,
        Long.MAX_VALUE,
    })
    void testDeserializeLong(long value) throws IOException {
        assertDeserializeValue(value, String.valueOf(value), StandardLongDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0",
        "+1",
        // Out of range
        "9223372036854775808",
        "-9223372036854775809",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeLongInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardLongDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0",
        "-0",
        "1",
        "-1",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        // Floating-point numbers that are integer
        "0.0",
        "-0.0",
        "1.0",
        "-1.0",
        "1e3",
        "-1e3",
        "1.0e3",
        "-1.0e3",
    })
    void testDeserializeBigInteger(String input) throws IOException {
        assertDeserializeValue(new BigDecimal(input).toBigIntegerExact(), input, StandardBigIntegerDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0",
        "+1",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeBigIntegerInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardBigIntegerDeserializer.INSTANCE);
    }

    // TODO #test: testDeserializeFloat

    // TODO #test: testDeserializeFloatInvalid

    // TODO #test: testDeserializeDouble

    // TODO #test: testDeserializeDoubleInvalid

    @ParameterizedTest
    @ValueSource(strings = {
        // Integer numbers
        "0",
        "-0",
        "1",
        "-1",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        // Floating-point numbers
        "0.0",
        "-0.0",
        "1.0",
        "-1.0",
        "0.1",
        "-0.1",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e+123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E+123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e-123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E-123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e+123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E+123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e-123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E-123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e+123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E+123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e-123",
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E-123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e+123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E+123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890e-123",
        "-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890E-123",
    })
    void testDeserializeBigDecimal(String input) throws IOException {
        assertDeserializeValue(new BigDecimal(input), input, StandardBigDecimalDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Plus sign
        "+0.0",
        "+1.0",
        // Missing integer part
        ".0",
        // Too many non-zero exponent digits
        "1e1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1E1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1e+1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1E+1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1e-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "1E-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1e1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1E1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1e+1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1E+1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1e-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        "-1E-1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
        // Other JSON value types
        "null",
        "true",
        "false",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeBigDecimalInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardBigDecimalDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(chars = {
        'a',
    })
    void testDeserializeChar(char value) throws IOException {
        assertDeserializeValue(value, quoted(String.valueOf(value)), StandardCharDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // More than one character
        "ab",
    })
    void testDeserializeCharInvalid(String value) {
        assertDeserializeValueInvalid(quoted(value), StandardCharDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @CsvSource({
        "'',''",
        "a,a",
        "abc def,abc def",
        "\uffff,\uffff",
        // Special escaped characters
        "\\\\,\\",
        "\\\",\"",
        "\\/,/",
        "\\b,'\b'",
        "\\f,'\f'",
        "\\n,'\n'",
        "\\r,'\r'",
        "\\t,'\t'",
        // Escaped code points
        "\\u0000,'\u0000'",
        "\\u1234,'\u1234'",
        "\\uabcd,'\uabcd'",
        "\\uffff,'\uffff'",
    })
    void testDeserializeString(String input, String value) throws IOException {
        assertDeserializeValue(value, quoted(input), StandardStringDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // No double quotes
        "a",
        // Single quotes
        "'a'",
        // Invalid syntax
        "\"",
        "\"a",
        "\"\\",
        "\"\\\"",
        // Control characters
        "\"\u0000\"",
        "\"\n\"",
        "\"\r\"",
        "\"\t\"",
        // Invalid escaped characters
        "\\a,a",
        // Invalid escaped code points
        "\\u1,u1",
        "\\u12,u12",
        "\\u123,u123",
        "\\u1x34,u1x34",
        "\\u12x4,u12x4",
        "\\u123x,u123x",
        // Other JSON value types
        "null",
        "true",
        "false",
        "0",
        "[]",
        "[0]",
        "{}",
    })
    void testDeserializeStringInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardStringDeserializer.INSTANCE);
    }

    // TODO #test: testDeserializeByteArray

    // TODO #test: testDeserializeByteArrayInvalid

    // TODO #test: testDeserializeLocalDate

    // TODO #test: testDeserializeLocalDateInvalid

    // TODO #test: testDeserializeLocalTime

    // TODO #test: testDeserializeLocalTimeInvalid

    // TODO #test: testDeserializeLocalDateTime

    // TODO #test: testDeserializeLocalDateTimeInvalid

    // TODO #test: testDeserializeOffsetDateTime

    // TODO #test: testDeserializeOffsetDateTimeInvalid

    @ParameterizedTest
    @MethodSource
    void testDeserializeSequence(String input, List<@Nullable Object> value) throws IOException {
        assertDeserializeValue(value, input, StandardListDeserializer.INSTANCE);
    }

    static Iterable<Arguments> testDeserializeSequence() {
        // NOTE: If numbers are used, remember the deserializer picks the smallest numeric type
        return List.of(
            arguments(
                "[]",
                List.of()
            ),
            arguments(
                "[true]",
                List.of(true)
            ),
            arguments(
                "[[], [[]], [[[]]]]",
                List.of(List.of(), List.of(List.of()), List.of(List.of(List.of())))
            )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Invalid syntax
        "[",
        "]",
        "[[",
        "[[]",
        "[,",
        "[,]",
        "[null,",
        "[null,]",
        "[,null",
        "[,null]",
        // Other JSON value types
        "null",
        "true",
        "false",
        "0",
        "\"\"",
        "\"0\"",
        "{}",
    })
    void testDeserializeSequenceInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardListDeserializer.INSTANCE);
    }

    @ParameterizedTest
    @MethodSource
    void testDeserializeMap(String input, Map<String, @Nullable Object> value) throws IOException {
        assertDeserializeValue(value, input, StandardMapDeserializer.INSTANCE);
    }

    static Iterable<Arguments> testDeserializeMap() {
        // NOTE: If numbers are used, remember the deserializer picks the smallest numeric type
        return List.of(
            arguments(
                "{}",
                Map.of()
            ),
            arguments(
                "{\"a\": true}",
                Map.of(
                    "a", true
                )
            ),
            arguments(
                "{\"a\": true, \"b\": [], \"c\": {}}",
                Map.of(
                    "a", true,
                    "b", List.of(),
                    "c", Map.of()
                )
            )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // Invalid syntax
        "{",
        "}",
        "{{",
        "{true: true}",
        "{1: true}",
        "{\"a\"",
        "{\"a\"}",
        "{\"a\" true}",
        "{\"a\":",
        "{\"a\":}",
        "{\"a\",",
        "{\"a\",}",
        "{\"a\", \"b\": true}",
        "{\"a\": true, \"b\"}",
        // Other JSON value types
        "null",
        "true",
        "false",
        "0",
        "\"\"",
        "\"0\"",
        "[]",
        "[0]",
    })
    void testDeserializeMapInvalid(String input) {
        assertDeserializeValueInvalid(input, StandardMapDeserializer.INSTANCE);
    }

    // TODO #test: testDeserializeStruct

    // TODO #test: testDeserializeStructInvalid
}
