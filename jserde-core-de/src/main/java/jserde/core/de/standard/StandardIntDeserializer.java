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
 * {@link StandardNumberDeserializer} for {@code int}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardIntDeserializer extends StandardNumberDeserializer<Integer> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardIntDeserializer INSTANCE = new StandardIntDeserializer();

    private StandardIntDeserializer() {}

    @Override
    public Integer deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeInt(this);
    }

    @Override
    public Integer visitInt(int value) {
        return value;
    }

    @Override
    public Integer visitLong(long value) throws IOException {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw numberOutOfRangeException("int", value);
        }
        return (int) value;
    }

    @Override
    public Integer visitBigInteger(BigInteger value) throws IOException {
        try {
            return value.intValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("int", value, e);
        }
    }

    @Override
    public Integer visitBigDecimal(BigDecimal value) throws IOException {
        try {
            return value.intValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("int", value, e);
        }
    }
}
