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

package jserde.core.ser.standard;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.time.LocalDate;
import jserde.core.DataType;
import jserde.core.ser.DataValueWriter;

/**
 * {@link StandardValueSerializer} that serializes a {@link LocalDate} to a {@link DataType#LOCAL_DATE}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardLocalDateSerializer extends StandardValueSerializer<LocalDate> {
    /**
     * Singleton instance of this class.
     */
    public static final StandardLocalDateSerializer INSTANCE = new StandardLocalDateSerializer();

    private StandardLocalDateSerializer() {}

    @Override
    public void serializeValue(LocalDate value, DataValueWriter writer) throws IOException {
        writer.serializeLocalDate(value);
    }
}
