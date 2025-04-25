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
 * JSON string.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonString extends JsonValue {
    private final String value;

    public JsonString(String value) {
        this.value = value;
    }

    /**
     * Returns {@link JsonValueType#STRING}.
     */
    @Override
    public JsonValueType getType() {
        return JsonValueType.STRING;
    }

    /**
     * Returns the value of this JSON string.
     *
     * @return the value of this JSON string
     */
    public String getValue() {
        return value;
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        writer.serializeString(value);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final JsonString other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return JsonString.class.getSimpleName() + '(' + value + ')';
    }
}
