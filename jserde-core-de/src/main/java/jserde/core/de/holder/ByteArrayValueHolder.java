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

package jserde.core.de.holder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import jserde.core.DataType;
import jserde.core.de.DataValueVisitor;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a fixed {@link DataType#BYTE_ARRAY}.
 *
 * @author Laurent Pireyn
 */
public final class ByteArrayValueHolder extends DataValueHolder {
    private final byte[] value;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ByteArrayValueHolder(byte[] value) {
        this.value = value;
    }

    @Override
    <T extends @Nullable Object> T deserializeValue(DataValueVisitor<T> visitor) throws IOException {
        return visitor.visitByteArray(value);
    }
}
