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

package jserde.core.de.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataValueReader;

/**
 * {@link StandardValueDeserializer} for {@code byte} array.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardByteArrayDeserializer extends StandardValueDeserializer<byte[]> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.BYTE_ARRAY));

    /**
     * Singleton instance of this class.
     */
    public static final StandardByteArrayDeserializer INSTANCE = new StandardByteArrayDeserializer();

    private StandardByteArrayDeserializer() {}

    @Override
    public byte[] deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeByteArray(this);
    }

    @Override
    public byte[] visitByteArray(byte[] value) {
        return value;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
