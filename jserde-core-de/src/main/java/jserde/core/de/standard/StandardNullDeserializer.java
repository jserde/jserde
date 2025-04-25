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
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.DataValueReader;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueDeserializer} for {@code null}.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class StandardNullDeserializer extends StandardValueDeserializer<@Nullable Void> {
    private static final Set<DataType> SUPPORTED_TYPES = unmodifiableSet(EnumSet.of(DataType.NULL));

    /**
     * Singleton instance of this class.
     */
    public static final StandardNullDeserializer INSTANCE = new StandardNullDeserializer();

    private StandardNullDeserializer() {}

    @Override
    public @Nullable Void deserializeValue(DataValueReader reader) throws IOException {
        return reader.deserializeNull(this);
    }

    @Override
    public @Nullable Void visitNull() {
        return null;
    }

    @Override
    public Set<DataType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
