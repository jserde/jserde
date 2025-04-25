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

package jserde.core.ser.holder;

import java.io.IOException;
import java.math.BigInteger;
import jserde.core.DataType;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a {@link DataType#BIG_INTEGER}.
 *
 * @author Laurent Pireyn
 */
public final class BigIntegerValueHolder extends DataValueHolder<BigInteger> {
    private @Nullable BigInteger value;

    @Override
    public void serializeBigInteger(BigInteger value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    BigInteger getValueInternal() {
        assert value != null;
        return value;
    }
}
