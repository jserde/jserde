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

package jserde.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JSerde data type.
 *
 * @author Laurent Pireyn
 */
public enum DataType {
    /**
     * The <i>null</i> value.
     *
     * <p>Typically corresponds to the Java {@code null} reference.
     */
    NULL,

    /**
     * The boolean values: <i>true</i> and <i>false</i>.
     *
     * <p>Corresponds to the Java {@code boolean} primitive.
     */
    BOOLEAN,

    /**
     * Signed bytes.
     *
     * <p>Corresponds to the Java {@code byte} primitive.
     */
    BYTE {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Signed 16-bit integers.
     *
     * <p>Corresponds to the Java {@code short} primitive.
     */
    SHORT {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Signed 32-bit integers.
     *
     * <p>Corresponds to the Java {@code int} primitive.
     */
    INT {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Signed 64-bit integers.
     *
     * <p>Corresponds to the Java {@code long} primitive.
     */
    LONG {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Arbitrary-precision signed integers.
     *
     * <p>Corresponds to the Java {@link BigInteger} class.
     */
    BIG_INTEGER {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Single-precision 32-bit IEEE 754 floating point numbers.
     *
     * <p>Corresponds to the Java {@code float} primitive.
     */
    FLOAT {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Double-precision 64-bit IEEE 754 floating point numbers.
     *
     * <p>Corresponds to the Java {@code double} primitive.
     */
    DOUBLE {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * Arbitrary-precision signed decimal numbers.
     *
     * <p>Corresponds to the Java {@link BigDecimal} class.
     */
    BIG_DECIMAL {
        @Override
        public boolean isNumeric() {
            return true;
        }
    },

    /**
     * 16-bit Unicode character.
     *
     * <p>Corresponds to the Java {@code char} primitive.
     */
    CHAR,

    /**
     * String of 16-bit Unicode characters.
     *
     * <p>Corresponds to the Java {@link String} class.
     */
    STRING,

    /**
     * Array of signed bytes.
     *
     * <p>Corresponds to the Java {@code byte[]} class.
     */
    BYTE_ARRAY,

    /**
     * Variably sized heterogeneous sequence of values.
     *
     * <p>Corresponds to the Java {@link List} interface.
     */
    SEQUENCE {
        @Override
        public boolean isContainer() {
            return true;
        }
    },

    /**
     * Variably sized heterogeneous key-value pairing.
     *
     * <p>Corresponds to the Java {@link Map} interface.
     */
    MAP {
        @Override
        public boolean isContainer() {
            return true;
        }
    },

    /**
     * Statically sized heterogeneous key-value pairing in which the keys are strings.
     */
    STRUCT {
        @Override
        public boolean isContainer() {
            return true;
        }
    };

    /**
     * Unmodifiable empty {@link Set} of data types.
     */
    public static final Set<DataType> NONE = unmodifiableSet(EnumSet.noneOf(DataType.class));

    /**
     * Unmodifiable {@link Set} of all data types.
     */
    public static final Set<DataType> ALL = unmodifiableSet(EnumSet.allOf(DataType.class));

    /**
     * Returns whether this data type is a numeric type.
     *
     * @return {@code true} if this data type is a numeric type,
     * {@code false otherwise}
     */
    public boolean isNumeric() {
        return false;
    }

    /**
     * Returns whether this data type is a container type.
     *
     * @return {@code true} if this data type is a container type,
     * {@code false otherwise}
     */
    public boolean isContainer() {
        return false;
    }
}
