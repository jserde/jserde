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
public final class JsonNull extends JsonValue {
    /**
     * Singleton instance of this class.
     */
    public static final JsonNull INSTANCE = new JsonNull();

    private JsonNull() {}

    /**
     * Returns {@link JsonValueType#NULL}.
     */
    @Override
    public JsonValueType getType() {
        return JsonValueType.NULL;
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        writer.serializeNull();
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
        return JsonNull.class.getSimpleName();
    }
}
