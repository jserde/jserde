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
 * {@link StandardNumberDeserializer} for {@code long}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardLongDeserializer extends StandardNumberDeserializer<Long> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardLongDeserializer INSTANCE = new StandardLongDeserializer();

    private StandardLongDeserializer() {}

    @Override
    public Long deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeLong(this);
    }

    @Override
    public Long visitLong(long value) {
        return value;
    }

    @Override
    public Long visitBigInteger(BigInteger value) throws IOException {
        try {
            return value.longValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("long", value, e);
        }
    }

    @Override
    public Long visitBigDecimal(BigDecimal value) throws IOException {
        try {
            return value.longValueExact();
        } catch (ArithmeticException e) {
            throw numberOutOfRangeException("long", value, e);
        }
    }
}
