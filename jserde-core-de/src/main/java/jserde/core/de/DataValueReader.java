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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import jserde.core.DataType;
import org.jspecify.annotations.Nullable;

/**
 * Format-specific data value reader.
 *
 * @author Laurent Pireyn
 * @see ValueDeserializer
 * @see DataValueVisitor
 */
public non-sealed interface DataValueReader extends DataReader, Closeable {
    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#NULL}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeNull(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BOOLEAN}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeBoolean(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BYTE}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeByte(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#SHORT}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeShort(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects an {@link DataType#INT}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeInt(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#LONG}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeLong(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BIG_INTEGER}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeBigInteger(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#FLOAT}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeFloat(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#DOUBLE}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeDouble(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BIG_DECIMAL}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeBigDecimal(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#CHAR}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeChar(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#STRING} given as a {@link String}.
     *
     * <p>The visitor should also support a string given as a {@link Reader}, possibly by relying on the default implementation.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeString(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#STRING} given as a {@link Reader}.
     *
     * <p>The visitor should also support a string given as a {@link String}.
     *
     * <p>The default implementation invokes {@link #deserializeString(DataValueVisitor)}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    default <T extends @Nullable Object> T deserializeStringAsReader(DataValueVisitor<T> visitor) throws IOException {
        return deserializeString(visitor);
    }

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BYTE_ARRAY} given as a {@code byte} array.
     *
     * <p>The visitor should also support a byte array given as an {@link InputStream}, possibly by relying on the default implementation.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeByteArray(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#BYTE_ARRAY} given as an {@link InputStream}.
     *
     * <p>The visitor should also support a byte array given as a {@code byte} array.
     *
     * <p>The default implementation invokes {@link #deserializeByteArray(DataValueVisitor)}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    default <T extends @Nullable Object> T deserializeByteArrayAsInputStream(DataValueVisitor<T> visitor) throws IOException {
        return deserializeByteArray(visitor);
    }

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#SEQUENCE}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeSequence(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#MAP}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeMap(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, hinting that it expects a {@link DataType#STRUCT}.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    <T extends @Nullable Object> T deserializeStruct(DataValueVisitor<T> visitor) throws IOException;

    /**
     * Deserializes a value with the given visitor, without specifying an expected {@link DataType}.
     *
     * <p>The default implementation throws a {@link DeserializationException}.
     * Implementing classes for self-describing formats should override this method.
     *
     * @param visitor the visitor
     * @param <T> the type of value produced by {@code visitor}
     * @return the deserialized value
     * @throws IOException if an I/O error occurs
     */
    default <T extends @Nullable Object> T deserializeAny(DataValueVisitor<T> visitor) throws IOException {
        throw new DeserializationException("This data value reader cannot determine the data type and must be hinted by the value deserializer"
            + " (hint: the value deserializer should invoke one of the deserialize* methods that hint to a data type)"
        );
    }
}
