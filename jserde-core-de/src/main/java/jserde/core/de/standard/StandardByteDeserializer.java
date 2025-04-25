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
 * {@link StandardNumberDeserializer} for {@code byte}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardByteDeserializer extends StandardNumberDeserializer<Byte> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardByteDeserializer INSTANCE = new StandardByteDeserializer();

    private StandardByteDeserializer() {}

    @Override
    public Byte deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeByte(this);
    }

    @Override
    public Byte visitByte(byte value) {
        return value;
    }

    @Override
    public Byte visitShort(short value) throws IOException {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            throw numberOutOfRangeException("byte", value);
        }
        return (byte) value;
    }

    @Override
    public Byte visitInt(int value) throws IOException {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            throw numberOutOfRangeException("byte", value);
        }
        return (byte) value;
    }

    @Override
    public Byte visitLong(long value) throws IOException {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            throw numberOutOfRangeException("byte", value);
        }
        return (byte) value;
    }

    @Override
    public Byte visitBigInteger(BigInteger value) throws IOException {
        try {
            return value.byteValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("byte", value, e);
        }
    }

    @Override
    public Byte visitBigDecimal(BigDecimal value) throws IOException {
        try {
            return value.byteValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("byte", value, e);
        }
    }
}
