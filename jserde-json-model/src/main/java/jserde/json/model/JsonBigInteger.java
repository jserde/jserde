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
import java.math.BigInteger;
import jserde.core.ser.DataValueWriter;

/**
 * {@link JsonNumber} on a {@link BigInteger} value.
 *
 * @author Laurent Pireyn
 */
@Immutable
final class JsonBigInteger extends JsonNumber {
    private final BigInteger value;

    JsonBigInteger(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        writer.serializeBigInteger(value);
    }
}
