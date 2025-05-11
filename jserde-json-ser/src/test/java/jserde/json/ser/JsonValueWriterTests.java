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

package jserde.json.ser;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import jserde.core.ser.ValueSerializer;
import jserde.core.ser.standard.StandardBigDecimalSerializer;
import jserde.core.ser.standard.StandardBigIntegerSerializer;
import jserde.core.ser.standard.StandardBooleanSerializer;
import jserde.core.ser.standard.StandardByteSerializer;
import jserde.core.ser.standard.StandardCharSerializer;
import jserde.core.ser.standard.StandardDoubleSerializer;
import jserde.core.ser.standard.StandardFloatSerializer;
import jserde.core.ser.standard.StandardIntSerializer;
import jserde.core.ser.standard.StandardLongSerializer;
import jserde.core.ser.standard.StandardNullSerializer;
import jserde.core.ser.standard.StandardObjectSerializer;
import jserde.core.ser.standard.StandardShortSerializer;
import jserde.core.ser.standard.StandardStringSerializer;
import jserde.core.ser.text.Indentation;
import jserde.core.ser.text.Newline;
import jserde.test.AbstractTests;
import org.jspecify.annotations.Nullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class JsonValueWriterTests extends AbstractTests {
    <T extends @Nullable Object> String serializeValue(T value, ValueSerializer<? super T> serializer) throws IOException {
        final var stringWriter = new StringWriter();
        try (
            stringWriter;
            var valueWriter = new JsonValueWriter(stringWriter)
        ) {
            serializer.serializeValue(value, valueWriter);
        }
        final var json = stringWriter.toString();
        log("JSON: " + json);
        return json;
    }

    @Test
    void testSerializeNull() throws IOException {
        assertEquals("null", serializeValue(null, StandardNullSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "true, 'true'",
        "false, 'false'",
    })
    void testSerializeBoolean(boolean value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardBooleanSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        Byte.MAX_VALUE + ", '" + Byte.MAX_VALUE + '\'',
        Byte.MIN_VALUE + ", '" + Byte.MIN_VALUE + '\'',
    })
    void testSerializeByte(byte value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardByteSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        Short.MAX_VALUE + ", '" + Short.MAX_VALUE + '\'',
        Short.MIN_VALUE + ", '" + Short.MIN_VALUE + '\'',
    })
    void testSerializeShort(short value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardShortSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        Integer.MAX_VALUE + ", '" + Integer.MAX_VALUE + '\'',
        Integer.MIN_VALUE + ", '" + Integer.MIN_VALUE + '\'',
    })
    void testSerializeInt(int value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardIntSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        Long.MAX_VALUE + ", '" + Long.MAX_VALUE + '\'',
        Long.MIN_VALUE + ", '" + Long.MIN_VALUE + '\'',
    })
    void testSerializeLong(long value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardLongSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        "12345678901234567890123456789012345678901234567890, '12345678901234567890123456789012345678901234567890'",
        "-12345678901234567890123456789012345678901234567890, '-12345678901234567890123456789012345678901234567890'",
    })
    void testSerializeBigInteger(BigInteger value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardBigIntegerSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0.0, '0'",
        "1.0, '1'",
        "-1.0, '-1'",
        "0.5, '0.5'",
        "-0.5, '-0.5'",
        "1.5, '1.5'",
        "-1.5, '-1.5'",
    })
    void testSerializeFloat(float value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardFloatSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0.0, '0'",
        "1.0, '1'",
        "-1.0, '-1'",
        "0.5, '0.5'",
        "-0.5, '-0.5'",
        "1.5, '1.5'",
        "-1.5, '-1.5'",
    })
    void testSerializeDouble(double value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardDoubleSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "0, '0'",
        "1, '1'",
        "-1, '-1'",
        "12345678901234567890123456789012345678901234567890, '12345678901234567890123456789012345678901234567890'",
        "-12345678901234567890123456789012345678901234567890, '-12345678901234567890123456789012345678901234567890'",
        "12345678901234567890123456789012345678901234567890.0, '12345678901234567890123456789012345678901234567890.0'",
        "-12345678901234567890123456789012345678901234567890.0, '-12345678901234567890123456789012345678901234567890.0'",
        "0.12345678901234567890123456789012345678901234567890, '0.12345678901234567890123456789012345678901234567890'",
        "-0.12345678901234567890123456789012345678901234567890, '-0.12345678901234567890123456789012345678901234567890'",
        "12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890, '12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890'",
        "-12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890, '-12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890'",
    })
    void testSerializeBigDecimal(BigDecimal value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardBigDecimalSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "'a', '\"a\"'",
        "'\n', '\"\\n\"'",
        "'\r', '\"\\r\"'",
        "'\t', '\"\\t\"'",
        "'\b', '\"\\b\"'",
        "'\f', '\"\\f\"'",
        "'\u0001', '\"\\u0001\"'",
    })
    void testSerializeChar(char value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardCharSerializer.INSTANCE));
    }

    @ParameterizedTest
    @CsvSource({
        "'', '\"\"'",
        "'a', '\"a\"'",
        "'abc', '\"abc\"'",
        "'a b c', '\"a b c\"'",
        "'\\\"\n\r\t\b\f\u0001', '\"\\\\\\\"\\n\\r\\t\\b\\f\\u0001\"'",
    })
    void testSerializeString(String value, String json) throws IOException {
        assertEquals(json, serializeValue(value, StandardStringSerializer.INSTANCE));
    }

    @ParameterizedTest
    @MethodSource
    void testJsonStyle(JsonStyle style) throws IOException {
        final var value = Map.of(
            "a", true,
            "b", 123,
            "c", "abc",
            "d", List.of(1, 2, 3),
            "e", Map.of(
                "x", 1,
                "y", 2,
                "z", List.of(1, 2, 3)
            ),
            "f", List.of(),
            "g", Map.of()
        );
        // TODO #test: Write proper test with assertions
        final var stringWriter = new StringWriter();
        try (
            stringWriter;
            var valueWriter = new JsonValueWriter(stringWriter, style)
        ) {
            StandardObjectSerializer.INSTANCE.serializeValue(value, valueWriter);
        }
        log("JSON:\n" + stringWriter);
    }

    static Iterable<JsonStyle> testJsonStyle() {
        return List.of(
            JsonStyle.MINIFIED,
            JsonStyle.PRETTY,
            JsonStyle.builder()
                .spaceAfterComma(true)
                .spaceAfterColon(true)
                .build(),
            JsonStyle.builder()
                .newline(Newline.crLf())
                .indentation(Indentation.none())
                .spaceAfterComma(true)
                .spaceAfterColon(true)
                .build()
        );
    }
}
