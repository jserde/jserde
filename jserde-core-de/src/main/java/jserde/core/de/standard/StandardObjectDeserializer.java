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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataMapReader;
import jserde.core.de.DataSequenceReader;
import jserde.core.de.DataStructReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.ValueDeserializer;
import org.jspecify.annotations.Nullable;

/**
 * Standard {@link ValueDeserializer} that deserializes values of any type to the corresponding values of standard type.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardObjectDeserializer extends StandardValueDeserializer<@Nullable Object> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardObjectDeserializer INSTANCE = new StandardObjectDeserializer();

    private StandardObjectDeserializer() {}

    @Override
    public @Nullable Object deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeAny(this);
    }

    @Override
    public @Nullable Void visitNull() {
        return StandardNullDeserializer.INSTANCE.visitNull();
    }

    @Override
    public Boolean visitBoolean(boolean value) {
        return StandardBooleanDeserializer.INSTANCE.visitBoolean(value);
    }

    @Override
    public Byte visitByte(byte value) {
        return StandardByteDeserializer.INSTANCE.visitByte(value);
    }

    @Override
    public Short visitShort(short value) {
        return StandardShortDeserializer.INSTANCE.visitShort(value);
    }

    @Override
    public Integer visitInt(int value) {
        return StandardIntDeserializer.INSTANCE.visitInt(value);
    }

    @Override
    public Long visitLong(long value) {
        return StandardLongDeserializer.INSTANCE.visitLong(value);
    }

    @Override
    public BigInteger visitBigInteger(BigInteger value) {
        return StandardBigIntegerDeserializer.INSTANCE.visitBigInteger(value);
    }

    @Override
    public Float visitFloat(float value) {
        return StandardFloatDeserializer.INSTANCE.visitFloat(value);
    }

    @Override
    public Double visitDouble(double value) {
        return StandardDoubleDeserializer.INSTANCE.visitDouble(value);
    }

    @Override
    public BigDecimal visitBigDecimal(BigDecimal value) {
        return StandardBigDecimalDeserializer.INSTANCE.visitBigDecimal(value);
    }

    @Override
    public Character visitChar(char value) {
        return StandardCharDeserializer.INSTANCE.visitChar(value);
    }

    @Override
    public String visitString(String value) {
        return StandardStringDeserializer.INSTANCE.visitString(value);
    }

    @Override
    public byte[] visitByteArray(byte[] value) {
        return StandardByteArrayDeserializer.INSTANCE.visitByteArray(value);
    }

    @Override
    public LocalDate visitLocalDate(LocalDate value) {
        return StandardLocalDateDeserializer.INSTANCE.visitLocalDate(value);
    }

    @Override
    public LocalTime visitLocalTime(LocalTime value) {
        return StandardLocalTimeDeserializer.INSTANCE.visitLocalTime(value);
    }

    @Override
    public LocalDateTime visitLocalDateTime(LocalDateTime value) {
        return StandardLocalDateTimeDeserializer.INSTANCE.visitLocalDateTime(value);
    }

    @Override
    public OffsetDateTime visitOffsetDateTime(OffsetDateTime value) {
        return StandardOffsetDateTimeDeserializer.INSTANCE.visitOffsetDateTime(value);
    }

    @Override
    public List<@Nullable Object> visitSequence(DataSequenceReader reader) throws IOException {
        return StandardListDeserializer.INSTANCE.visitSequence(reader);
    }

    @Override
    public Map<@Nullable Object, @Nullable Object> visitMap(DataMapReader reader) throws IOException {
        return StandardMapDeserializer.INSTANCE.visitMap(reader);
    }

    @Override
    public Map<String, @Nullable Object> visitStruct(DataStructReader reader) throws IOException {
        return StandardStructDeserializer.INSTANCE.visitStruct(reader);
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return DataType.ALL;
    }
}
