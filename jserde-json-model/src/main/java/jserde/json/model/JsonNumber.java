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
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.json.JsonValueType;
import org.jspecify.annotations.Nullable;

/**
 * JSON number.
 *
 * @author Laurent Pireyn
 */
@Immutable
public abstract sealed class JsonNumber extends JsonValue permits JsonByte, JsonShort, JsonInt, JsonLong, JsonBigInteger, JsonFloat, JsonDouble, JsonBigDecimal {
    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(byte value) {
        return new JsonByte(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(short value) {
        return new JsonShort(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(int value) {
        return new JsonInt(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(long value) {
        return new JsonLong(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(BigInteger value) {
        return new JsonBigInteger(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(float value) {
        return new JsonFloat(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(double value) {
        return new JsonDouble(value);
    }

    /**
     * Returns a JSON number with the given value.
     *
     * @param value the value
     * @return a JSON number with {@code value}
     */
    public static JsonNumber of(BigDecimal value) {
        return new JsonBigDecimal(value);
    }

    /**
     * Returns {@link JsonValueType#NUMBER}.
     */
    @Override
    public final JsonValueType getType() {
        return JsonValueType.NUMBER;
    }

    /**
     * Returns the value of this JSON number.
     *
     * @return the value of this JSON number
     */
    public abstract Number getValue();

    // NOTE: The equality depends on the actual value type, i.e. JsonByte(1) != JsonInt(1)

    @Override
    public final boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final JsonNumber other)) {
            return false;
        }
        return getValue().equals(other.getValue());
    }

    @Override
    public final int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public final String toString() {
        return JsonNumber.class.getSimpleName() + '(' + getValue() + ')';
    }
}
