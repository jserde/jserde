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

package jserde.json.model;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.core.de.DataMapReader;
import jserde.core.de.DataSequenceReader;
import jserde.core.de.DataStructReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.core.de.ValueDeserializer;

/**
 * {@link ValueDeserializer} for {@link JsonValue}s.
 *
 * <p>Although JSON values are obviously tightly related to the JSON format,
 * this deserializer works with any format.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonValueDeserializer implements ValueDeserializer<JsonValue>, DataValueVisitor<JsonValue> {
    /**
     * The singleton instance of this class.
     */
    public static final JsonValueDeserializer INSTANCE = new JsonValueDeserializer();

    private JsonValueDeserializer() {}

    @Override
    public JsonValue deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeAny(this);
    }

    @Override
    public JsonNull visitNull() {
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonBoolean visitBoolean(boolean value) {
        return JsonBoolean.of(value);
    }

    @Override
    public JsonNumber visitByte(byte value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitShort(short value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitInt(int value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitLong(long value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitBigInteger(BigInteger value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitFloat(float value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitDouble(double value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonNumber visitBigDecimal(BigDecimal value) {
        return JsonNumber.of(value);
    }

    @Override
    public JsonString visitChar(char value) {
        return new JsonString(String.valueOf(value));
    }

    @Override
    public JsonString visitString(String value) {
        return new JsonString(value);
    }

    @Override
    public JsonValue visitByteArray(byte[] value) {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonArray visitSequence(DataSequenceReader reader) throws IOException {
        final var sizeHint = reader.getSizeHint();
        final var jsonArray = new JsonArray(sizeHint >= 0 ? sizeHint : JsonArray.DEFAULT_INITIAL_CAPACITY);
        while (reader.hasNextElement()) {
            jsonArray.add(reader.nextElement(this));
        }
        return jsonArray;
    }

    @Override
    public JsonObject visitMap(DataMapReader reader) {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonObject visitStruct(DataStructReader reader) throws IOException {
        final var sizeHint = reader.getSizeHint();
        final var jsonObject = new JsonObject(sizeHint >= 0 ? sizeHint : JsonObject.DEFAULT_INITIAL_CAPACITY);
        while (reader.hasNextField()) {
            jsonObject.put(reader.nextFieldName(), reader.fieldValue(this));
        }
        return jsonObject;
    }
}
