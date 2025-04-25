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
 * {@link StandardValueSerializer} that serializes a {@code byte} array to a {@link DataType#BYTE_ARRAY}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardByteArraySerializer extends StandardValueSerializer<byte[]> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardByteArraySerializer INSTANCE = new StandardByteArraySerializer();

    private StandardByteArraySerializer() {}

    @Override
    public void serializeValue(byte[] value, DataValueWriter writer) throws IOException {
        writer.serializeByteArray(value);
    }
}
