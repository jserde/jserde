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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.StringWriter;
import jserde.core.ser.DataValueWriter;
import jserde.json.JsonValueType;
import jserde.json.de.JsonValueReader;
import jserde.json.ser.JsonValueWriter;
import org.jspecify.annotations.Nullable;

/**
 * JSON value.
 *
 * @author Laurent Pireyn
 */
public abstract sealed class JsonValue permits JsonNull, JsonBoolean, JsonNumber, JsonString, JsonArray, JsonObject {
    /**
     * Returns the JSON value parsed from the given JSON text.
     *
     * @param json the JSON text
     * @return the JSON value parsed from {@code json}
     * @throws IllegalArgumentException if {@code json} is invalid
     */
    @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS")
    public static JsonValue parseJsonValue(String json) {
        try {
            return JsonValueDeserializer.INSTANCE.deserializeValue(new JsonValueReader(json));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON text: " + json, e);
        }
    }

    /**
     * Returns the {@link JsonValueType} of this JSON value.
     *
     * @return the type of this value
     */
    public abstract JsonValueType getType();

    /**
     * Serializes this JSON value to the given {@link DataValueWriter}.
     *
     * <p>Although JSON values are obviously tightly related to the JSON format,
     * any data value writer can be used.
     *
     * @param writer the data value writer
     * @throws IOException if an I/O error occurs
     */
    public abstract void serialize(DataValueWriter writer) throws IOException;

    /**
     * Returns the JSON representation of this JSON value.
     *
     * @return the JSON representation of this JSON value
     */
    public final String toJson() {
        // TODO #improvement: Use LightStringWriter from jserde-io when available
        // TODO: Extract constant for initial capacity
        final var stringWriter = new StringWriter(100);
        try (
            stringWriter;
            var valueWriter = new JsonValueWriter(stringWriter)
        ) {
            serialize(valueWriter);
        } catch (IOException e) {
            // This should not happen
            throw new AssertionError(e);
        }
        return stringWriter.toString();
    }

    @Override
    public abstract boolean equals(@Nullable Object object);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
