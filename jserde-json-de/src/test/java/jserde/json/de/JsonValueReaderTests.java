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

    <T extends @Nullable Object> T deserializeValue(ValueDeserializer<T> deserializer, String input) throws IOException {
        log("Input: " + input);
        final var value = deserializer.deserializeValue(new JsonValueReader(input));
        // TODO #test #improvement: Invoke Arrays.toString for arrays
        log("Deserialized value: " + value);
        return value;
    }

    <T extends @Nullable Object> void checkDeserializeValue(ValueDeserializer<T> deserializer, String input, T expected) throws IOException {
        assertEquals(expected, deserializeValue(deserializer, input));
    }

    void checkDeserializeValueInvalid(ValueDeserializer<?> deserializer, String input) {
        final var exception = assertThrows(IOException.class, () -> deserializeValue(deserializer, input));
        logExpectedException(exception);
    }

    @Test
    void testDeserializeNull() throws IOException {
        checkDeserializeValue(StandardNullDeserializer.INSTANCE, "null", null);
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
        checkDeserializeValueInvalid(StandardNullDeserializer.INSTANCE, input);
    }

    @ParameterizedTest
    @ValueSource(booleans = {
        true,
        false,
    })
    void testDeserializeBoolean(boolean value) throws IOException {
        checkDeserializeValue(StandardBooleanDeserializer.INSTANCE, String.valueOf(value), value);
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
        checkDeserializeValueInvalid(StandardBooleanDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardByteDeserializer.INSTANCE, String.valueOf(value), value);
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
        checkDeserializeValueInvalid(StandardByteDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardShortDeserializer.INSTANCE, String.valueOf(value), value);
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
        checkDeserializeValueInvalid(StandardShortDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardIntDeserializer.INSTANCE, String.valueOf(value), value);
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
        checkDeserializeValueInvalid(StandardIntDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardLongDeserializer.INSTANCE, String.valueOf(value), value);
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
        checkDeserializeValueInvalid(StandardLongDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardBigIntegerDeserializer.INSTANCE, input, new BigDecimal(input).toBigIntegerExact());
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
        checkDeserializeValueInvalid(StandardBigIntegerDeserializer.INSTANCE, input);
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
        checkDeserializeValue(StandardBigDecimalDeserializer.INSTANCE, input, new BigDecimal(input));
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
        checkDeserializeValueInvalid(StandardBigDecimalDeserializer.INSTANCE, input);
    }

    @ParameterizedTest
    @ValueSource(chars = {
        'a',
    })
    void testDeserializeChar(char value) throws IOException {
        checkDeserializeValue(StandardCharDeserializer.INSTANCE, quoted(String.valueOf(value)), value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Empty
        "",
        // More than one character
        "ab",
    })
    void testDeserializeCharInvalid(String value) {
        checkDeserializeValueInvalid(StandardCharDeserializer.INSTANCE, quoted(value));
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
        checkDeserializeValue(StandardStringDeserializer.INSTANCE, quoted(input), value);
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
        checkDeserializeValueInvalid(StandardStringDeserializer.INSTANCE, input);
    }

    // TODO #test: testDeserializeByteArray

    // TODO #test: testDeserializeByteArrayInvalid

    @ParameterizedTest
    @MethodSource
    void testDeserializeSequence(String input, List<@Nullable Object> value) throws IOException {
        checkDeserializeValue(StandardListDeserializer.INSTANCE, input, value);
    }

    static Iterable<Arguments> testDeserializeSequence() {
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
        checkDeserializeValueInvalid(StandardListDeserializer.INSTANCE, input);
    }

    // TODO #test: testDeserializeMap

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
        checkDeserializeValueInvalid(StandardMapDeserializer.INSTANCE, input);
    }

    // TODO #test: testDeserializeStruct

    // TODO #test: testDeserializeStructInvalid
}
