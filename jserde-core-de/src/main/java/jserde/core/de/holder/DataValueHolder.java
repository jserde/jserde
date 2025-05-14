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

package jserde.core.de.holder;

import com.google.errorprone.annotations.ForOverride;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import org.jspecify.annotations.Nullable;

/**
 * Abstract {@link DataValueReader} that holds a fixed value.
 *
 * @author Laurent Pireyn
 */
public abstract sealed class DataValueHolder implements DataValueReader permits NullValueHolder, BooleanValueHolder, ByteValueHolder, ShortValueHolder, IntValueHolder, LongValueHolder, BigIntegerValueHolder, FloatValueHolder, DoubleValueHolder, BigDecimalValueHolder, CharValueHolder, StringValueHolder, ByteArrayValueHolder, LocalDateValueHolder, LocalTimeValueHolder, LocalDateTimeValueHolder, OffsetDateTimeValueHolder, SequenceValueHolder, MapValueHolder, StructValueHolder {
    /**
     * Returns a data value holder that holds the given fixed value.
     *
     * @param value the value
     * @return a data value holder that holds {@code value}
     * @throws IllegalArgumentException if {@code value} is of an unsupported type
     */
    public static DataValueHolder on(@Nullable Object value) {
        if (value == null) {
            return new NullValueHolder();
        }
        if (value instanceof final Boolean valueAsBoolean) {
            return new BooleanValueHolder(valueAsBoolean);
        }
        if (value instanceof final Byte valueAsByte) {
            return new ByteValueHolder(valueAsByte);
        }
        if (value instanceof final Short valueAsShort) {
            return new ShortValueHolder(valueAsShort);
        }
        if (value instanceof final Integer valueAsInt) {
            return new IntValueHolder(valueAsInt);
        }
        if (value instanceof final Long valueAsLong) {
            return new LongValueHolder(valueAsLong);
        }
        if (value instanceof final BigInteger valueAsBigInteger) {
            return new BigIntegerValueHolder(valueAsBigInteger);
        }
        if (value instanceof final Float valueAsFloat) {
            return new FloatValueHolder(valueAsFloat);
        }
        if (value instanceof final Double valueAsDouble) {
            return new DoubleValueHolder(valueAsDouble);
        }
        if (value instanceof final BigDecimal valueAsBigDecimal) {
            return new BigDecimalValueHolder(valueAsBigDecimal);
        }
        if (value instanceof final Character valueAsChar) {
            return new CharValueHolder(valueAsChar);
        }
        if (value instanceof final String valueAsString) {
            return new StringValueHolder(valueAsString);
        }
        if (value instanceof final byte[] valueAsByteArray) {
            return new ByteArrayValueHolder(valueAsByteArray);
        }
        if (value instanceof final LocalDate valueAsLocalDate) {
            return new LocalDateValueHolder(valueAsLocalDate);
        }
        if (value instanceof final LocalTime valueAsLocalTime) {
            return new LocalTimeValueHolder(valueAsLocalTime);
        }
        if (value instanceof final LocalDateTime valueAsLocalDateTime) {
            return new LocalDateTimeValueHolder(valueAsLocalDateTime);
        }
        if (value instanceof final OffsetDateTime valueAsOffsetDateTime) {
            return new OffsetDateTimeValueHolder(valueAsOffsetDateTime);
        }
        if (value instanceof final Collection<?> valueAsCollection) {
            return new SequenceValueHolder(valueAsCollection);
        }
        if (value instanceof final Map<?, ?> valueAsMap) {
            return new MapValueHolder(valueAsMap);
        }
        throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
    }

    private boolean closed;

    @Override
    public final <T extends @Nullable Object> T deserializeNull(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeBoolean(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeByte(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeShort(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeInt(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeLong(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeBigInteger(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeFloat(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeDouble(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeBigDecimal(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeChar(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeString(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeByteArray(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeLocalDate(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeLocalTime(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeLocalDateTime(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeOffsetDateTime(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeSequence(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeMap(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeStruct(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    @Override
    public final <T extends @Nullable Object> T deserializeAny(DataValueVisitor<T> visitor) throws IOException {
        return deserializeValueIfNotDone(visitor);
    }

    private <T extends @Nullable Object> T deserializeValueIfNotDone(DataValueVisitor<T> visitor) throws IOException {
        if (closed) {
            throw new EOFException();
        }
        closed = true;
        return deserializeValue(visitor);
    }

    @ForOverride
    abstract <T extends @Nullable Object> T deserializeValue(DataValueVisitor<T> visitor) throws IOException;

    @Override
    public final void close() {
        closed = true;
    }
}
