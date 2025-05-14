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

package jserde.core.ser.holder;

import java.io.IOException;
import java.time.LocalDateTime;
import jserde.core.DataType;
import org.jspecify.annotations.Nullable;

/**
 * {@link DataValueHolder} that holds a {@link DataType#LOCAL_DATE_TIME}.
 *
 * @author Laurent Pireyn
 */
public final class LocalDateTimeValueHolder extends DataValueHolder<LocalDateTime> {
    private @Nullable LocalDateTime value;

    @Override
    public void serializeLocalDateTime(LocalDateTime value) throws IOException {
        beforeSerializeValue();
        this.value = value;
    }

    @Override
    LocalDateTime getValueInternal() {
        assert value != null;
        return value;
    }
}
