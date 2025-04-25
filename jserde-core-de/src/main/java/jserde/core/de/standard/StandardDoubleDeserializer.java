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
 * {@link StandardNumberDeserializer} for {@code double}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardDoubleDeserializer extends StandardNumberDeserializer<Double> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardDoubleDeserializer INSTANCE = new StandardDoubleDeserializer();

    private StandardDoubleDeserializer() {}

    @Override
    public Double deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeDouble(this);
    }

    @Override
    public Double visitByte(byte value) {
        return (double) value;
    }

    @Override
    public Double visitShort(short value) {
        return (double) value;
    }

    @Override
    public Double visitInt(int value) {
        return (double) value;
    }

    @Override
    public Double visitLong(long value) {
        return (double) value;
    }

    @Override
    public Double visitBigInteger(BigInteger value) {
        return value.doubleValue();
    }

    @Override
    public Double visitFloat(float value) {
        return (double) value;
    }

    @Override
    public Double visitDouble(double value) {
        return value;
    }

    @Override
    public Double visitBigDecimal(BigDecimal value) {
        return value.doubleValue();
    }
}
