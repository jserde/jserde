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
import jserde.core.ser.DataValueWriter;
import jserde.json.JsonValueType;
import org.jspecify.annotations.Nullable;

/**
 * JSON null.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonBoolean extends JsonValue {
    /**
     * The instance of this class for {@code true}.
     */
    public static final JsonBoolean TRUE = new JsonBoolean(true);

    /**
     * The instance of this class for {@code false}.
     */
    public static final JsonBoolean FALSE = new JsonBoolean(false);

    /**
     * Returns the instance of this class for the given value.
     *
     * @param value the value
     * @return the instance of this class for {@code value}
     */
    public static JsonBoolean of(boolean value) {
        return value ? TRUE : FALSE;
    }

    private final boolean value;

    private JsonBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Returns {@link JsonValueType#BOOLEAN}.
     */
    @Override
    public JsonValueType getType() {
        return JsonValueType.BOOLEAN;
    }

    /**
     * Returns the value of this JSON boolean.
     *
     * @return the value of this JSON boolean
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        writer.serializeBoolean(value);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return this == object;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return JsonBoolean.class.getSimpleName() + '(' + value + ')';
    }
}
