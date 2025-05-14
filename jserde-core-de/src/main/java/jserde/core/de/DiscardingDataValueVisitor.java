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

package jserde.core.de;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Set;
import jserde.core.DataType;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueVisitor} that discards all visited values.
 *
 * @author Laurent Pireyn
 */
public final class DiscardingDataValueVisitor implements DataValueVisitor<@Nullable Void> {
    /**
     * Singleton instance of this class.
     */
    public static final DiscardingDataValueVisitor INSTANCE = new DiscardingDataValueVisitor();

    private DiscardingDataValueVisitor() {}

    @Override
    public @Nullable Void visitNull() {
        return null;
    }

    @Override
    public @Nullable Void visitBoolean(boolean value) {
        return null;
    }

    @Override
    public @Nullable Void visitByte(byte value) {
        return null;
    }

    @Override
    public @Nullable Void visitShort(short value) {
        return null;
    }

    @Override
    public @Nullable Void visitInt(int value) {
        return null;
    }

    @Override
    public @Nullable Void visitLong(long value) {
        return null;
    }

    @Override
    public @Nullable Void visitBigInteger(BigInteger value) {
        return null;
    }

    @Override
    public @Nullable Void visitFloat(float value) {
        return null;
    }

    @Override
    public @Nullable Void visitDouble(double value) {
        return null;
    }

    @Override
    public @Nullable Void visitBigDecimal(BigDecimal value) {
        return null;
    }

    @Override
    public @Nullable Void visitChar(char value) {
        return null;
    }

    @Override
    public @Nullable Void visitString(String value) {
        return null;
    }

    @Override
    public @Nullable Void visitString(Reader reader, int lengthHint) throws IOException {
        reader.close();
        return null;
    }

    @Override
    public @Nullable Void visitByteArray(byte[] value) {
        return null;
    }

    @Override
    public @Nullable Void visitByteArray(InputStream input, int lengthHint) throws IOException {
        input.close();
        return null;
    }

    @Override
    public @Nullable Void visitLocalDate(LocalDate value) {
        return null;
    }

    @Override
    public @Nullable Void visitLocalTime(LocalTime value) {
        return null;
    }

    @Override
    public @Nullable Void visitLocalDateTime(LocalDateTime value) {
        return null;
    }

    @Override
    public @Nullable Void visitOffsetDateTime(OffsetDateTime value) {
        return null;
    }

    @Override
    public @Nullable Void visitSequence(DataSequenceReader reader) {
        return null;
    }

    @Override
    public @Nullable Void visitMap(DataMapReader reader) {
        return null;
    }

    @Override
    public @Nullable Void visitStruct(DataStructReader reader) {
        return null;
    }

    @Override
    public InvalidValueException unsupportedTypeException(DataType type) {
        // This should not happen
        throw new AssertionError(type);
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return DataType.ALL;
    }
}
