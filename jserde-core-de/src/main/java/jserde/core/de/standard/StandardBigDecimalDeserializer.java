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
 * {@link StandardNumberDeserializer} for {@link BigDecimal}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardBigDecimalDeserializer extends StandardNumberDeserializer<BigDecimal> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardBigDecimalDeserializer INSTANCE = new StandardBigDecimalDeserializer();

    private StandardBigDecimalDeserializer() {}

    @Override
    public BigDecimal deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeBigDecimal(this);
    }

    @Override
    public BigDecimal visitByte(byte value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitShort(short value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitInt(int value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitLong(long value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitBigInteger(BigInteger value) {
        return new BigDecimal(value);
    }

    @Override
    public BigDecimal visitFloat(float value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitDouble(double value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal visitBigDecimal(BigDecimal value) {
        return value;
    }
}
