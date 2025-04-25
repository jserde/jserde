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
import java.math.BigDecimal;
import java.math.BigInteger;
import jserde.core.de.DataValueReader;

/**
 * {@link StandardNumberDeserializer} for {@link BigInteger}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardBigIntegerDeserializer extends StandardNumberDeserializer<BigInteger> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardBigIntegerDeserializer INSTANCE = new StandardBigIntegerDeserializer();

    private StandardBigIntegerDeserializer() {}

    @Override
    public BigInteger deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeBigInteger(this);
    }

    @Override
    public BigInteger visitByte(byte value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger visitShort(short value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger visitInt(int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger visitLong(long value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger visitBigInteger(BigInteger value) {
        return value;
    }

    @Override
    public BigInteger visitBigDecimal(BigDecimal value) throws IOException {
        try {
            return value.toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException(BigInteger.class.getSimpleName(), value, e);
        }
    }
}
