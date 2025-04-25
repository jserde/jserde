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

package jserde.json.de;

import com.google.errorprone.annotations.ForOverride;
import com.google.errorprone.annotations.MustBeClosed;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import jserde.core.de.DataContainerReader;
import jserde.core.de.DataSequenceReader;
import jserde.core.de.DataStructReader;
import jserde.core.de.DataValueReader;
import jserde.core.de.DataValueVisitor;
import jserde.core.de.DeserializationException;
import jserde.core.de.InvalidValueException;
import jserde.core.de.ValueDeserializer;
import jserde.core.de.ValueDiscarder;
import jserde.io.AbstractReader;
import jserde.io.LightCharArrayReader;
import jserde.io.LightCharArrayWriter;
import jserde.json.JsonFormat;
import org.jspecify.annotations.Nullable;

/**
 * JSON {@link DataValueReader}.
 *
 * @author Laurent Pireyn
 */
public class JsonValueReader implements DataValueReader {
    private static final char QUOTATION_MARK = '"';
    private static final char BACKSLASH = '\\';
    private static final char LSB = '[';
    private static final char RSB = ']';
    private static final char LCB = '{';
    private static final char RCB = '}';
    private static final char COMMA = ',';
    private static final char COLON = ':';

    // WS characters (sorted)
    private static final char BS = '\b';
    private static final char HT = '\t';
    private static final char FF = '\f';
    private static final char LF = '\n';
    private static final char CR = '\r';
    private static final char SP = ' ';

    // TODO #improvement: Make max nesting depth configurable
    private static final int MAX_NESTING_DEPTH = 100;

    private static boolean isWs(int c) {
        // TODO #optimization: Order these tests by descending probability
        return c == SP
            || c == LF
            || c == CR
            || c == HT;
    }

    private static boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    private static BigDecimal toBigDecimal(String string) throws DeserializationException {
        try {
            return new BigDecimal(string);
        } catch (NumberFormatException e) {
            throw new DeserializationException("The number cannot be represented as a " + BigDecimal.class.getSimpleName() + ": " + string, e);
        }
    }

    private static String quoted(char c) {
        return "'" + c + '\'';
    }

    private static String description(JsonToken token) {
        return switch (token) {
            case NULL -> "null";
            case TRUE, FALSE -> "boolean";
            case NUMBER_BEGIN -> "number";
            case STRING_BEGIN -> "string";
            case ARRAY_BEGIN -> "array";
            case OBJECT_BEGIN -> "object";
        };
    }

    /**
     * {@link Reader} on a JSON string.
     */
    private final class JsonStringReader extends AbstractReader {
        private boolean closed;

        /**
         * Reopens this reader so the same instance can be reused.
         */
        void reopen() {
            closed = false;
        }

        @Override
        public int read() throws IOException {
            if (closed) {
                // This has already been closed
                return -1;
            }
            final int c;
            try {
                c = readCharInString();
            } catch (IOException e) {
                // Mark this as closed, so we don't try to skip the remaining characters in the close() method
                closed = true;
                throw e;
            }
            if (c < 0) {
                // End of string, mark this as closed
                assert c == -1;
                closed = true;
            }
            return c;
        }

        @Override
        public void close() throws IOException {
            if (!closed) {
                closed = true;
                skipRemainingChars();
            }
        }

        private void skipRemainingChars() throws IOException {
            while (readCharInString() != -1) {}
        }
    }

    /**
     * {@link DataContainerReader} on a JSON container.
     */
    private abstract sealed class JsonContainerReader implements Closeable permits JsonSequenceReader, JsonStructReader {
        @MustBeClosed
        JsonContainerReader() {}

        @Override
        public final void close() throws IOException {
            afterCloseContainerReader();
            skipRemainingChildren();
        }

        @ForOverride
        abstract void skipRemainingChildren() throws IOException;
    }

    /**
     * {@link DataSequenceReader} on a JSON array.
     */
    private final class JsonSequenceReader extends JsonContainerReader implements DataSequenceReader {
        private boolean hasNextElement;

        @MustBeClosed
        JsonSequenceReader() throws IOException {
            final var c = readNonWsChar();
            if (c != RSB) {
                unreadChar(c);
                hasNextElement = true;
            }
        }

        @Override
        public boolean hasNextElement() {
            return hasNextElement;
        }

        @Override
        public <T extends @Nullable Object> T nextElement(ValueDeserializer<T> deserializer) throws IOException {
            if (!hasNextElement) {
                throw new NoSuchElementException("No remaining elements in sequence"
                    + " (hint: invoke hasNextElement() to know if there are more elements)"
                );
            }
            final T value;
            try {
                value = deserializer.deserializeValue(JsonValueReader.this);
                final var c = readNonWsChar();
                if (c == COMMA) {
                    hasNextElement = true;
                } else if (c == RSB) {
                    hasNextElement = false;
                } else {
                    throw invalidCharException(c, quoted(COMMA) + " or " + quoted(RSB));
                }
            } catch (IOException e) {
                // Mark this as ended, so we don't try to skip the remaining elements
                hasNextElement = false;
                throw e;
            }
            return value;
        }

        @Override
        protected void skipRemainingChildren() throws IOException {
            while (hasNextElement()) {
                nextElement(ValueDiscarder.INSTANCE);
            }
        }
    }

    /**
     * {@link DataStructReader} on a JSON object.
     */
    private final class JsonStructReader extends JsonContainerReader implements DataStructReader {
        private @Nullable String fieldName;
        private boolean fieldValueDeserialized = true;

        @MustBeClosed
        JsonStructReader() throws IOException {
            final var c = readNonWsChar();
            if (c == QUOTATION_MARK) {
                fieldName = getStringValue();
            } else if (c != RCB) {
                throw invalidCharException(c, quoted(QUOTATION_MARK) + " or " + quoted(RCB));
            }
        }

        @Override
        public boolean hasNextField() {
            return fieldName != null;
        }

        @Override
        public String nextFieldName() {
            if (fieldName == null) {
                throw new NoSuchElementException("No remaining fields in struct"
                    + " (hint: invoke hasNextField() to know if there are more fields)"
                );
            }
            if (!fieldValueDeserialized) {
                throw new IllegalStateException("The value of field \"" + fieldName + "\" was not deserialized"
                    + " (hint: invoke fieldValue(...) after each invocation of nextFieldName())"
                );
            }
            fieldValueDeserialized = false;
            return fieldName;
        }

        @Override
        public <T extends @Nullable Object> T fieldValue(ValueDeserializer<T> deserializer) throws IOException {
            if (fieldName == null) {
                throw new IllegalStateException("No remaining fields in struct"
                    + " (hint: invoke hasNextField() to know if there are more fields)"
                );
            }
            if (fieldValueDeserialized) {
                throw new IllegalStateException("The value of field \"" + fieldName + "\" has already been deserialized"
                    + " (hint: invoke nextFieldName() before each invocation of fieldValue(...))"
                );
            }
            fieldValueDeserialized = true;
            final T value;
            try {
                var c = readNonWsChar();
                if (c != COLON) {
                    throw invalidCharException(c, quoted(COLON));
                }
                value = deserializer.deserializeValue(JsonValueReader.this);
                c = readNonWsChar();
                if (c == COMMA) {
                    c = readNonWsChar();
                    if (c != QUOTATION_MARK) {
                        throw invalidCharException(c, quoted(QUOTATION_MARK));
                    }
                    fieldName = getStringValue();
                } else if (c == RCB) {
                    fieldName = null;
                } else {
                    throw invalidCharException(c, quoted(COMMA) + " or " + quoted(RCB));
                }
            } catch (IOException e) {
                // Mark this as ended, so we don't try to skip the remaining fields
                fieldName = null;
                fieldValueDeserialized = true;
                throw e;
            }
            return value;
        }

        @Override
        protected void skipRemainingChildren() throws IOException {
            if (!fieldValueDeserialized) {
                // The field name has been determined but the field value has not been deserialized
                fieldValue(ValueDiscarder.INSTANCE);
            }
            while (hasNextField()) {
                nextFieldName();
                fieldValue(ValueDiscarder.INSTANCE);
            }
        }
    }

    private final Reader reader;

    /**
     * Unread character.
     *
     * <p>-2 means none;
     * -1 means EOF.
     */
    private int unreadChar = -2;

    /**
     * Position of the last character read from {@link #reader}.
     *
     * <p>-1 means no character was read yet.
     */
    private long position = -1L;

    /**
     * Line of the last character read from {@link #reader} (1-based).
     *
     * <p>Only meaningful if {@link #position} is not -1.
     */
    private int line = 1;

    /**
     * Position of the first character on the line of the last character read from {@link #reader}.
     *
     * <p>Only meaningful if {@link #position} is not -1.
     *
     * <p>The column is the difference between {@link #position} and this (+1 as it is 1-based).
     */
    private long bolPosition;

    // TODO: Extract constant for initial capacity
    private final LightCharArrayWriter valueWriter = new LightCharArrayWriter(1000);

    /**
     * Reusable string reader.
     */
    private final JsonStringReader stringReader = new JsonStringReader();

    /**
     * Current nesting depth.
     */
    private int nestingDepth;

    /**
     * Creates a new {@code JsonValueReader} on the given {@link Reader}.
     *
     * @param reader the reader
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Reader subclasses require to be closed
    public JsonValueReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * Creates a new {@code JsonValueReader} on the given {@code char} array.
     *
     * @param array the array
     */
    public JsonValueReader(char[] array) {
        this(new LightCharArrayReader(array));
    }

    /**
     * Creates a new {@code JsonValueReader} on the given {@link String}.
     *
     * @param string the string
     */
    public JsonValueReader(String string) {
        // TODO #optimization: Use LightStringReader from jserde-io when available
        this(new StringReader(string));
    }

    /**
     * Creates a new {@code JsonValueReader} on the given {@link InputStream},
     * using {@link JsonFormat#DEFAULT_CHARSET}.
     *
     * @param input the input stream
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all InputStream subclasses require to be closed
    public JsonValueReader(InputStream input) {
        this(new InputStreamReader(input, JsonFormat.DEFAULT_CHARSET));
    }

    // Reader-related methods

    private int readChar() throws IOException {
        final var c = reader.read();
        if (c >= 0) {
            // Not EOF, so increment position
            ++position;
        }
        return c;
    }

    private void readCharExpecting(char expectedCh) throws IOException {
        final var c = readChar();
        if (c != expectedCh) {
            throw invalidCharException(c, quoted(expectedCh));
        }
    }

    private int readNonWsChar() throws IOException {
        int c;
        // Determine next character
        final var unreadChar = this.unreadChar;
        if (unreadChar == -2) {
            // No unread character, so read the next character from the reader
            c = readChar();
        } else {
            // Use unread character
            c = unreadChar;
            this.unreadChar = -2;
        }
        if (c < 0) {
            // EOF
            assert c == -1;
            return -1;
        }
        // Read characters from the reader as long as they are WS
        int prevC = 0;
        while (true) {
            if (prevC == LF) {
                // The previous character was LF
                // This character starts a new line
                ++line;
                bolPosition = position;
            } else if (prevC == CR) {
                // The previous character was CR
                if (c == LF) {
                    // This character is LF, so it is part of a CRLF sequence and doesn't start a new line
                    // LF is WS, so continue the loop early
                    prevC = c;
                    c = readChar();
                    continue;
                }
                // This character is not LF, so it starts a new line
                ++line;
                bolPosition = position;
            }
            // This character doesn't start a new line
            if (isWs(c)) {
                // This character is WS, so continue the loop
                prevC = c;
                c = readChar();
                continue;
            }
            // This character is not WS, so return it
            return c;
        }
    }

    private void unreadChar(int c) {
        assert unreadChar == -2;
        unreadChar = c;
    }

    private int getLine() {
        return position >= 0L ? line : -1;
    }

    private int getColumn() {
        return position >= 0L ? ((int) (position - bolPosition)) + 1 : -1;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    // Token-related methods

    private JsonToken readToken() throws IOException {
        final var c = readNonWsChar();
        // TODO #optimization: Order these tests by descending probability
        // Boolean?
        if (c == 't') {
            return consumeTrueToken();
        }
        if (c == 'f') {
            return consumeFalseToken();
        }
        // String?
        if (c == QUOTATION_MARK) {
            return JsonToken.STRING_BEGIN;
        }
        // Number?
        if (isDigit(c) || c == '-') {
            unreadChar(c);
            return JsonToken.NUMBER_BEGIN;
        }
        // Array?
        if (c == LSB) {
            return JsonToken.ARRAY_BEGIN;
        }
        // Object?
        if (c == LCB) {
            return JsonToken.OBJECT_BEGIN;
        }
        // Null?
        if (c == 'n') {
            return consumeNullToken();
        }
        if (c < 0) {
            // EOF
            assert c == -1;
            throw new EOFException();
        }
        throw invalidCharException(c, "any value");
    }

    private JsonToken consumeNullToken() throws IOException {
        // FIXME: Consuming the token early messes up the line and column in the exception
        readCharExpecting('u');
        readCharExpecting('l');
        readCharExpecting('l');
        return JsonToken.NULL;
    }

    private JsonToken consumeTrueToken() throws IOException {
        // FIXME: Consuming the token early messes up the line and column in the exception
        readCharExpecting('r');
        readCharExpecting('u');
        readCharExpecting('e');
        return JsonToken.TRUE;
    }

    private JsonToken consumeFalseToken() throws IOException {
        // FIXME: Consuming the token early messes up the line and column in the exception
        readCharExpecting('a');
        readCharExpecting('l');
        readCharExpecting('s');
        readCharExpecting('e');
        return JsonToken.FALSE;
    }

    private void readTokenExpecting(JsonToken expected) throws IOException {
        final var token = readToken();
        if (token != expected) {
            throw invalidValueException(token, description(expected));
        }
    }

    // JSON number-related methods

    private String getNumberValueAsString() throws IOException {
        final var valueWriter = this.valueWriter;
        valueWriter.reset();
        assert isDigit(unreadChar) || unreadChar == '-';
        int c = unreadChar;
        unreadChar = -2;
        // Integer part
        // Optional minus sign
        if (c == '-') {
            valueWriter.write(c);
            c = readChar();
        }
        // First digit
        if (!isDigit(c)) {
            throw invalidCharException(c, "digit ('0'-'9')");
        }
        valueWriter.write(c);
        if (c == '0') {
            // The first digit is zero
            c = readChar();
        } else {
            // The first digit is not zero
            // Optional next digits
            while (true) {
                c = readChar();
                if (!isDigit(c)) {
                    break;
                }
                valueWriter.write(c);
            }
        }
        // Optional fraction part
        if (c == '.') {
            valueWriter.write(c);
            // First fraction digit
            c = readChar();
            if (!isDigit(c)) {
                throw invalidCharException(c, "digit ('0'-'9')");
            }
            // Optional next fraction digits
            do {
                valueWriter.write(c);
                c = readChar();
            } while (isDigit(c));
        }
        // Optional exponent part
        if (c == 'e' || c == 'E') {
            valueWriter.write(c);
            // Optional exponent sign
            c = readChar();
            if (c == '+' || c == '-') {
                valueWriter.write(c);
                c = readChar();
            }
            // First exponent digit
            if (!isDigit(c)) {
                throw invalidCharException(c, "exponent digit ('0'-'9')");
            }
            // Optional next exponent digits
            do {
                valueWriter.write(c);
                c = readChar();
            } while (isDigit(c));
        }
        unreadChar(c);
        return valueWriter.toString();
    }

    // JSON string-related methods

    private int readCharInString() throws IOException {
        final var c = readChar();
        if (c == QUOTATION_MARK) {
            // End of string
            return -1;
        }
        if (c == BACKSLASH) {
            // Escaped character
            return readEscapedChar();
        }
        if (c < SP) {
            // EOF or control character
            assert c >= -1;
            throw invalidCharException(c, "non-control character");
        }
        // Non-control character
        return c;
    }

    private int readEscapedChar() throws IOException {
        final var c = readChar();
        // TODO #optimization: Order these tests by descending probability
        if (c == QUOTATION_MARK || c == BACKSLASH) {
            return c;
        }
        if (c == 'n') {
            return LF;
        }
        if (c == 'r') {
            return CR;
        }
        if (c == 't') {
            return HT;
        }
        if (c == 'u') {
            return readHexDigit() << 12
                | readHexDigit() << 8
                | readHexDigit() << 4
                | readHexDigit();
        }
        if (c == '/') {
            return c;
        }
        if (c == 'f') {
            return FF;
        }
        if (c == 'b') {
            return BS;
        }
        throw invalidCharException(c, "escaped character ('\\', '\"', '/', 'b', 'f', 'n', 'r', 't' or 'u')");
    }

    private int readHexDigit() throws IOException {
        final var c = readChar();
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        throw invalidCharException(c, "hexadecimal digit ('0'-'9', 'a'-'f' or 'A'-'F')");
    }

    @MustBeClosed
    private Reader openStringReader() {
        final var stringReader = this.stringReader;
        stringReader.reopen();
        return stringReader;
    }

    private String getStringValue() throws IOException {
        final var valueWriter = this.valueWriter;
        valueWriter.reset();
        while (true) {
            final var c = readCharInString();
            if (c < 0) {
                // End of string
                assert c == -1;
                return valueWriter.toString();
            }
            valueWriter.write(c);
        }
    }

    // Exception-related methods

    private InvalidCharException invalidCharException(int c, String expected) {
        return new InvalidCharException(c, getLine(), getColumn(), expected);
    }

    private InvalidJsonValueException invalidValueException(JsonToken token, String expected) {
        return new InvalidJsonValueException(description(token), getLine(), getColumn(), expected);
    }

    // DataValueReader methods

    @Override
    public <T extends @Nullable Object> T deserializeNull(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NULL);
        return visitor.visitNull();
    }

    @Override
    public <T extends @Nullable Object> T deserializeBoolean(DataValueVisitor<T> visitor) throws IOException {
        final boolean value;
        final var token = readToken();
        if (token == JsonToken.TRUE) {
            value = true;
        } else if (token == JsonToken.FALSE) {
            value = false;
        } else {
            throw invalidValueException(token, "boolean");
        }
        return visitor.visitBoolean(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeByte(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        byte value;
        try {
            value = Byte.parseByte(valueAsString);
        } catch (NumberFormatException ignored) {
            final var valueAsBigDecimal = toBigDecimal(valueAsString);
            try {
                value = valueAsBigDecimal.byteValueExact();
            } catch (ArithmeticException e) {
                throw new InvalidValueException("The number is out of byte range: " + valueAsString, e);
            }
        }
        return visitor.visitByte(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeShort(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        short value;
        try {
            value = Short.parseShort(valueAsString);
        } catch (NumberFormatException ignored) {
            final var valueAsBigDecimal = toBigDecimal(valueAsString);
            try {
                value = valueAsBigDecimal.shortValueExact();
            } catch (ArithmeticException e) {
                throw new InvalidValueException("The number is out of short range: " + valueAsString, e);
            }
        }
        return visitor.visitShort(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeInt(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        int value;
        try {
            value = Integer.parseInt(valueAsString);
        } catch (NumberFormatException ignored) {
            final var valueAsBigDecimal = toBigDecimal(valueAsString);
            try {
                value = valueAsBigDecimal.intValueExact();
            } catch (ArithmeticException e) {
                throw new InvalidValueException("The number is out of int range: " + valueAsString, e);
            }
        }
        return visitor.visitInt(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeLong(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        long value;
        try {
            value = Long.parseLong(valueAsString);
        } catch (NumberFormatException ignored) {
            final var valueAsBigDecimal = toBigDecimal(valueAsString);
            try {
                value = valueAsBigDecimal.longValueExact();
            } catch (ArithmeticException e) {
                throw new InvalidValueException("The number is out of long range: " + valueAsString, e);
            }
        }
        return visitor.visitLong(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeBigInteger(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        BigInteger value;
        try {
            value = new BigInteger(valueAsString);
        } catch (NumberFormatException ignored) {
            final var valueAsBigDecimal = toBigDecimal(valueAsString);
            try {
                value = valueAsBigDecimal.toBigIntegerExact();
            } catch (ArithmeticException e) {
                throw new InvalidValueException("The number is not an integer: " + valueAsString, e);
            }
        }
        return visitor.visitBigInteger(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeFloat(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        final float value;
        try {
            value = Float.parseFloat(valueAsString);
        } catch (NumberFormatException e) {
            throw new InvalidValueException("The number is out of float range: " + valueAsString, e);
        }
        return visitor.visitFloat(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeDouble(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        final var valueAsString = getNumberValueAsString();
        final double value;
        try {
            value = Double.parseDouble(valueAsString);
        } catch (NumberFormatException e) {
            throw new InvalidValueException("The number is out of double range: " + valueAsString, e);
        }
        return visitor.visitDouble(value);
    }

    @Override
    public <T extends @Nullable Object> T deserializeBigDecimal(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.NUMBER_BEGIN);
        return visitor.visitBigDecimal(toBigDecimal(getNumberValueAsString()));
    }

    @Override
    public <T extends @Nullable Object> T deserializeChar(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.STRING_BEGIN);
        return visitWithChar(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeString(DataValueVisitor<T> visitor) throws IOException {
        return deserializeStringAsReader(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeStringAsReader(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.STRING_BEGIN);
        return visitWithString(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeByteArray(DataValueVisitor<T> visitor) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends @Nullable Object> T deserializeSequence(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.ARRAY_BEGIN);
        return visitWithSequence(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeMap(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.OBJECT_BEGIN);
        return visitWithMap(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeStruct(DataValueVisitor<T> visitor) throws IOException {
        readTokenExpecting(JsonToken.OBJECT_BEGIN);
        return visitWithStruct(visitor);
    }

    @Override
    public <T extends @Nullable Object> T deserializeAny(DataValueVisitor<T> visitor) throws IOException {
        final var token = readToken();
        return switch (token) {
            // TODO #optimization: Order these cases by descending probability
            case NULL -> visitor.visitNull();
            case TRUE -> visitor.visitBoolean(true);
            case FALSE -> visitor.visitBoolean(false);
            case NUMBER_BEGIN -> visitWithNumber(visitor);
            case STRING_BEGIN -> visitWithString(visitor);
            case ARRAY_BEGIN -> visitWithSequence(visitor);
            case OBJECT_BEGIN -> visitWithStruct(visitor);
        };
    }

    // Container readers-related methods

    private void beforeOpenContainerReader() throws DeserializationException {
        if (nestingDepth == MAX_NESTING_DEPTH) {
            throw new DeserializationException("The maximum nesting depth of " + MAX_NESTING_DEPTH + " has been reached");
        }
        ++nestingDepth;
    }

    private void afterCloseContainerReader() {
        --nestingDepth;
        assert nestingDepth >= 0;
    }

    @MustBeClosed
    // TODO: Don't suppress MustBeClosedChecker when it supports this flow
    @SuppressWarnings("MustBeClosedChecker")
    private JsonSequenceReader openSequenceReader() throws IOException {
        beforeOpenContainerReader();
        return new JsonSequenceReader();
    }

    @MustBeClosed
    // TODO: Don't suppress MustBeClosedChecker when it supports this flow
    @SuppressWarnings("MustBeClosedChecker")
    private JsonStructReader openStructReader() throws IOException {
        beforeOpenContainerReader();
        return new JsonStructReader();
    }

    // DataValueVisitor-related methods

    private <T extends @Nullable Object> T visitWithNumber(DataValueVisitor<T> visitor) throws IOException {
        final var value = toBigDecimal(getNumberValueAsString());
        var converted = false;
        // Byte?
        byte valueAsByte = 0;
        try {
            valueAsByte = value.byteValueExact();
            converted = true;
        } catch (ArithmeticException e) {
            // Fall through
        }
        if (converted) {
            return visitor.visitByte(valueAsByte);
        }
        // Short?
        short valueAsShort = 0;
        try {
            valueAsShort = value.shortValueExact();
            converted = true;
        } catch (ArithmeticException e) {
            // Fall through
        }
        if (converted) {
            return visitor.visitShort(valueAsShort);
        }
        // Int?
        int valueAsInt = 0;
        try {
            valueAsInt = value.intValueExact();
            converted = true;
        } catch (ArithmeticException e) {
            // Fall through
        }
        if (converted) {
            return visitor.visitInt(valueAsInt);
        }
        // Long?
        long valueAsLong = 0;
        try {
            valueAsLong = value.longValueExact();
            converted = true;
        } catch (ArithmeticException e) {
            // Fall through
        }
        if (converted) {
            return visitor.visitLong(valueAsLong);
        }
        // Big integer?
        var valueAsBigInteger = BigInteger.ZERO;
        try {
            valueAsBigInteger = value.toBigIntegerExact();
            converted = true;
        } catch (ArithmeticException e) {
            // Fall through
        }
        if (converted) {
            return visitor.visitBigInteger(valueAsBigInteger);
        }
        // NOTE: We don't attempt to convert to float or double, as these conversions are lossy and always succeed
        // Big decimal
        return visitor.visitBigDecimal(value);
    }

    private <T extends @Nullable Object> T visitWithChar(DataValueVisitor<T> visitor) throws IOException {
        final char ch;
        try (var reader = openStringReader()) {
            final var c = reader.read();
            if (c < 0) {
                // End of string
                assert c == -1;
                throw new InvalidValueException("The string is empty; expected one-character string");
            }
            if (reader.read() >= 0) {
                // Not end of string
                throw new InvalidValueException("The string has more than one character; expected one-character string");
            }
            ch = (char) c;
        }
        return visitor.visitChar(ch);
    }

    private <T extends @Nullable Object> T visitWithString(DataValueVisitor<T> visitor) throws IOException {
        try (var reader = openStringReader()) {
            return visitor.visitString(reader, -1);
        }
    }

    private <T extends @Nullable Object> T visitWithSequence(DataValueVisitor<T> visitor) throws IOException {
        try (var sequenceReader = openSequenceReader()) {
            return visitor.visitSequence(sequenceReader);
        }
    }

    private <T extends @Nullable Object> T visitWithMap(DataValueVisitor<T> visitor) throws IOException {
        try (var structReader = openStructReader()) {
            return visitor.visitMap(structReader.asMapReader());
        }
    }

    private <T extends @Nullable Object> T visitWithStruct(DataValueVisitor<T> visitor) throws IOException {
        try (var structReader = openStructReader()) {
            return visitor.visitStruct(structReader);
        }
    }
}
