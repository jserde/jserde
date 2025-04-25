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
 * {@link StandardNumberDeserializer} for {@code float}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardFloatDeserializer extends StandardNumberDeserializer<Float> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardFloatDeserializer INSTANCE = new StandardFloatDeserializer();

    private StandardFloatDeserializer() {}

    @Override
    public Float deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeFloat(this);
    }

    @Override
    public Float visitByte(byte value) {
        return (float) value;
    }

    @Override
    public Float visitShort(short value) {
        return (float) value;
    }

    @Override
    public Float visitInt(int value) {
        return (float) value;
    }

    @Override
    public Float visitLong(long value) {
        return (float) value;
    }

    @Override
    public Float visitBigInteger(BigInteger value) {
        return value.floatValue();
    }

    @Override
    public Float visitFloat(float value) {
        return value;
    }

    @Override
    public Float visitDouble(double value) {
        return (float) value;
    }

    @Override
    public Float visitBigDecimal(BigDecimal value) {
        return value.floatValue();
    }
}
