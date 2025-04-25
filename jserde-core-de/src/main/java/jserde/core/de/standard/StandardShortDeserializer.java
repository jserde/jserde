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
 * {@link StandardNumberDeserializer} for {@code short}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardShortDeserializer extends StandardNumberDeserializer<Short> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardShortDeserializer INSTANCE = new StandardShortDeserializer();

    private StandardShortDeserializer() {}

    @Override
    public Short deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeShort(this);
    }

    @Override
    public Short visitShort(short value) {
        return value;
    }

    @Override
    public Short visitInt(int value) throws IOException {
        if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
            throw numberOutOfRangeException("short", value);
        }
        return (short) value;
    }

    @Override
    public Short visitLong(long value) throws IOException {
        if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
            throw numberOutOfRangeException("short", value);
        }
        return (short) value;
    }

    @Override
    public Short visitBigInteger(BigInteger value) throws IOException {
        try {
            return value.shortValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("short", value, e);
        }
    }

    @Override
    public Short visitBigDecimal(BigDecimal value) throws IOException {
        try {
            return value.shortValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("short", value, e);
        }
    }
}
