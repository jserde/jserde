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

import java.util.Set;
import jserde.core.DataType;
import jserde.core.de.InvalidValueException;
import org.jspecify.annotations.Nullable;

/**
 * {@link StandardValueDeserializer} for numbers.
 *
 * @author Laurent Pireyn
 * @param <T> the type of number produced by this deserializer
 */
public abstract sealed class StandardNumberDeserializer<T extends Number> extends StandardValueDeserializer<T> permits StandardByteDeserializer, StandardShortDeserializer, StandardIntDeserializer, StandardLongDeserializer, StandardBigIntegerDeserializer, StandardFloatDeserializer, StandardDoubleDeserializer, StandardBigDecimalDeserializer {
    static InvalidValueException numberOutOfRangeException(String type, Number value) {
        return numberOutOfRangeException(type, value, null);
    }

    static InvalidValueException numberOutOfRangeException(String type, Number value, @Nullable Exception cause) {
        return new InvalidValueException("The number is out of " + type + " range: " + value, cause);
    }

    @Override
    public final Set<DataType> getSupportedTypes() {
        return DataType.NUMERIC;
    }
}
