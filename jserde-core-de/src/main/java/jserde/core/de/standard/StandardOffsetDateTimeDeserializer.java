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
import java.time.OffsetDateTime;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataValueReader;

/**
 * {@link StandardValueDeserializer} for {@link OffsetDateTime}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardOffsetDateTimeDeserializer extends StandardValueDeserializer<OffsetDateTime> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.OFFSET_DATE_TIME));

    /**
     * Singleton instance of this class.
     */
    public static final StandardOffsetDateTimeDeserializer INSTANCE = new StandardOffsetDateTimeDeserializer();

    private StandardOffsetDateTimeDeserializer() {}

    @Override
    public OffsetDateTime deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeOffsetDateTime(this);
    }

    @Override
    public OffsetDateTime visitOffsetDateTime(OffsetDateTime value) {
        return value;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
