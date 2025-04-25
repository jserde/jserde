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

package jserde.core.ser.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import jserde.core.DataType;
import jserde.core.ser.DataValueWriter;

/**
 * {@link StandardValueSerializer} that serializes a {@code long} to a {@link DataType#LONG}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardLongSerializer extends StandardValueSerializer<Long> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardLongSerializer INSTANCE = new StandardLongSerializer();

    private StandardLongSerializer() {}

    @Override
    public void serializeValue(Long value, DataValueWriter writer) throws IOException {
        writer.serializeLong(value);
    }
}
