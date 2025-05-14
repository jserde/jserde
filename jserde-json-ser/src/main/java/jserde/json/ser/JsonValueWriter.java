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

package jserde.json.ser;

import com.google.errorprone.annotations.ForOverride;
import com.google.errorprone.annotations.MustBeClosed;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Locale;
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.DataValueWriter;
import jserde.core.ser.SerializationException;
import jserde.core.ser.ValueSerializer;
import jserde.io.AbstractWriter;
import jserde.json.JsonFormat;
import static jserde.json.JsonSyntax.BACKSLASH;
import static jserde.json.JsonSyntax.BS;
import static jserde.json.JsonSyntax.COLON;
import static jserde.json.JsonSyntax.COMMA;
import static jserde.json.JsonSyntax.CR;
import static jserde.json.JsonSyntax.FALSE;
import static jserde.json.JsonSyntax.FF;
import static jserde.json.JsonSyntax.HT;
import static jserde.json.JsonSyntax.LCB;
import static jserde.json.JsonSyntax.LF;
import static jserde.json.JsonSyntax.LSB;
import static jserde.json.JsonSyntax.NULL;
import static jserde.json.JsonSyntax.QUOTATION_MARK;
import static jserde.json.JsonSyntax.RCB;
import static jserde.json.JsonSyntax.RSB;
import static jserde.json.JsonSyntax.TRUE;
import org.jspecify.annotations.Nullable;

/**
 * JSON {@link DataValueWriter}.
 *
 * @author Laurent Pireyn
 */
public final class JsonValueWriter implements DataValueWriter {
    public static final JsonStyle DEFAULT_STYLE = JsonStyle.MINIFIED;

    public static final int DEFAULT_MAX_NESTING_DEPTH = 500;

    private final class JsonStringWriter extends AbstractWriter {
        // TODO: Consider using java.util.HexFormat
        private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        void reopen() throws IOException {
            writer.write(QUOTATION_MARK);
        }

        @Override
        public void write(int c) throws IOException {
            switch (c) {
                case QUOTATION_MARK, BACKSLASH -> {
                    writer.write(BACKSLASH);
                    writer.write(c);
                }
                case LF -> {
                    writer.write(BACKSLASH);
                    writer.write('n');
                }
                case CR -> {
                    writer.write(BACKSLASH);
                    writer.write('r');
                }
                case HT -> {
                    writer.write(BACKSLASH);
                    writer.write('t');
                }
                case FF -> {
                    writer.write(BACKSLASH);
                    writer.write('f');
                }
                case BS -> {
                    writer.write(BACKSLASH);
                    writer.write('b');
                }
                // NOTE: Slash ('/') is not escaped
                default -> {
                    if (c < ' ') {
                        // Control character
                        writer.write(BACKSLASH);
                        writer.write('u');
                        writer.write(HEX_DIGITS[(c & 0xf000) >> 12]);
                        writer.write(HEX_DIGITS[(c & 0x0f00) >> 8]);
                        writer.write(HEX_DIGITS[(c & 0x00f0) >> 4]);
                        writer.write(HEX_DIGITS[c & 0x000f]);
                    } else {
                        // Non-control character
                        writer.write(c);
                    }
                }
            }
        }

        @Override
        public void close() throws IOException {
            writer.write(QUOTATION_MARK);
        }
    }

    private abstract sealed class JsonContainerWriter implements Closeable permits JsonArrayWriter, JsonObjectWriter {
        private boolean closed;

        @MustBeClosed
        @SuppressFBWarnings({
            "CT_CONSTRUCTOR_THROW",
            "PCOA_PARTIALLY_CONSTRUCTED_OBJECT_ACCESS",
        })
        JsonContainerWriter() throws IOException {
            writeContainerBegin();
        }

        @ForOverride
        abstract void writeContainerBegin() throws IOException;

        @Override
        public final void close() throws IOException {
            if (!closed) {
                closed = true;
                afterCloseContainerWriter();
                writeContainerEnd();
            }
        }

        @ForOverride
        abstract void writeContainerEnd() throws IOException;
    }

    private final class JsonArrayWriter extends JsonContainerWriter implements DataSequenceWriter {
        private int elementCount;

        @MustBeClosed
        JsonArrayWriter() throws IOException {}

        @Override
        void writeContainerBegin() throws IOException {
            writer.write(LSB);
        }

        @Override
        public <T extends @Nullable Object> void serializeElement(T value, ValueSerializer<? super T> serializer) throws IOException {
            if (elementCount > 0) {
                writer.write(COMMA);
                afterComma();
            }
            beforeContainerChild();
            serializer.serializeValue(value, JsonValueWriter.this);
            ++elementCount;
        }

        @Override
        void writeContainerEnd() throws IOException {
            if (!style.isCollapseEmptyContainers() || elementCount > 0) {
                beforeContainerChild();
            }
            writer.write(RSB);
        }
    }

    private final class JsonObjectWriter extends JsonContainerWriter implements DataStructWriter {
        private int fieldCount;

        @MustBeClosed
        JsonObjectWriter() throws IOException {}

        @Override
        void writeContainerBegin() throws IOException {
            writer.write(LCB);
        }

        @Override
        public <T extends @Nullable Object> void serializeField(String name, T value, ValueSerializer<? super T> serializer) throws IOException {
            if (fieldCount > 0) {
                writer.write(COMMA);
                afterComma();
            }
            beforeContainerChild();
            serializeString(name);
            writer.write(COLON);
            afterColon();
            serializer.serializeValue(value, JsonValueWriter.this);
            ++fieldCount;
        }

        @Override
        void writeContainerEnd() throws IOException {
            if (!style.isCollapseEmptyContainers() || fieldCount > 0) {
                beforeContainerChild();
            }
            writer.write(RCB);
        }
    }

    private final Writer writer;
    private final JsonStyle style;

    // NOTE: Number formats are instance fields because NumberFormat is not thread-safe
    // TODO #optimization: Find a more efficient way to format floating-point numbers

    /**
     * {@link DecimalFormat} used for {@code float} values.
     */
    private final DecimalFormat floatFormat = new DecimalFormat("0.##########", DecimalFormatSymbols.getInstance(Locale.ROOT));

    /**
     * {@link DecimalFormat} used for {@code double} values.
     */
    private final DecimalFormat doubleFormat = new DecimalFormat("0.####################", DecimalFormatSymbols.getInstance(Locale.ROOT));

    /**
     * Reusable JSON string {@link Writer}.
     */
    private final JsonStringWriter stringWriter = new JsonStringWriter();

    /**
     * Maximum nesting depth.
     */
    private int maxNestingDepth = DEFAULT_MAX_NESTING_DEPTH;

    /**
     * Current nesting depth.
     */
    private int nestingDepth;

    /**
     * Creates a new {@code JsonValueWriter} on the given {@link Writer},
     * using {@link #DEFAULT_STYLE}.
     *
     * @param writer the writer
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    public JsonValueWriter(Writer writer) {
        this(writer, DEFAULT_STYLE);
    }

    /**
     * Creates a new {@code JsonValueWriter} on the given {@link Writer},
     * using the given {@link JsonStyle}.
     *
     * @param writer the writer
     * @param style the style
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JsonValueWriter(Writer writer, JsonStyle style) {
        this.writer = writer;
        this.style = style;
    }

    /**
     * Creates a new {@code JsonValueWriter} on the given {@link OutputStream},
     * using {@link JsonFormat#DEFAULT_CHARSET} and {@link #DEFAULT_STYLE}.
     *
     * @param output the output stream
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    public JsonValueWriter(OutputStream output) {
        this(output, DEFAULT_STYLE);
    }

    /**
     * Creates a new {@code JsonValueWriter} on the given {@link OutputStream},
     * using {@link JsonFormat#DEFAULT_CHARSET} and the given {@link JsonStyle}.
     *
     * @param output the output stream
     * @param style the style
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all Writer subclasses require to be closed
    public JsonValueWriter(OutputStream output, JsonStyle style) {
        this(new OutputStreamWriter(output, JsonFormat.DEFAULT_CHARSET), style);
    }

    public int getMaxNestingDepth() {
        return maxNestingDepth;
    }

    public void setMaxNestingDepth(int maxNestingDepth) {
        this.maxNestingDepth = maxNestingDepth;
    }

    // DataValueWriter methods

    @Override
    public void serializeNull() throws IOException {
        writer.write(NULL);
    }

    @Override
    public void serializeBoolean(boolean value) throws IOException {
        writer.write(value ? TRUE : FALSE);
    }

    @Override
    public void serializeByte(byte value) throws IOException {
        writer.write(String.valueOf(value));
    }

    @Override
    public void serializeShort(short value) throws IOException {
        writer.write(String.valueOf(value));
    }

    @Override
    public void serializeInt(int value) throws IOException {
        writer.write(String.valueOf(value));
    }

    @Override
    public void serializeLong(long value) throws IOException {
        writer.write(String.valueOf(value));
    }

    @Override
    public void serializeBigInteger(BigInteger value) throws IOException {
        writer.write(value.toString());
    }

    @Override
    public void serializeFloat(float value) throws IOException {
        writer.write(floatFormat.format(value));
    }

    @Override
    public void serializeDouble(double value) throws IOException {
        writer.write(doubleFormat.format(value));
    }

    @Override
    public void serializeBigDecimal(BigDecimal value) throws IOException {
        writer.write(value.toString());
    }

    @Override
    public void serializeChar(char value) throws IOException {
        try (var writer = openJsonStringWriter()) {
            writer.write(value);
        }
    }

    @Override
    public void serializeString(String value) throws IOException {
        try (var writer = openJsonStringWriter()) {
            writer.write(value);
        }
    }

    @Override
    public void serializeString(Reader reader, int lengthHint) throws IOException {
        try (
            reader;
            var writer = openJsonStringWriter()
        ) {
            reader.transferTo(writer);
        }
    }

    @Override
    // TODO: Remove this annotation
    @SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
    public void serializeByteArray(byte[] value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    // TODO: Remove this annotation
    @SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
    public void serializeLocalDate(LocalDate value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    // TODO: Remove this annotation
    @SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
    public void serializeLocalTime(LocalTime value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    // TODO: Remove this annotation
    @SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
    public void serializeLocalDateTime(LocalDateTime value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    // TODO: Remove this annotation
    @SuppressFBWarnings("BED_BOGUS_EXCEPTION_DECLARATION")
    public void serializeOffsetDateTime(OffsetDateTime value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int size) throws IOException {
        return openJsonArrayWriter();
    }

    @Override
    @MustBeClosed
    // TODO: Don't suppress MustBeClosedChecker when it supports this flow
    @SuppressWarnings("MustBeClosedChecker")
    public DataMapWriter serializeMap(int size) throws IOException {
        return openJsonObjectWriter().asMapWriter();
    }

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int fieldCount) throws IOException {
        return openJsonObjectWriter();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    // Child writers-related methods

    @MustBeClosed
    private JsonStringWriter openJsonStringWriter() throws IOException {
        stringWriter.reopen();
        return stringWriter;
    }

    private void beforeOpenContainerWriter() throws SerializationException {
        if (maxNestingDepth >= 0) {
            // NOTE: We don't increment nestingDepth if maxNestingDepth is unlimited
            if (nestingDepth == maxNestingDepth) {
                throw new SerializationException("The maximum nesting depth of " + maxNestingDepth + " has been reached");
            }
            ++nestingDepth;
        }
    }

    private void afterCloseContainerWriter() {
        if (maxNestingDepth >= 0) {
            // NOTE: We don't decrement nestingDepth if maxNestingDepth is unlimited
            --nestingDepth;
            assert nestingDepth >= 0;
        }
    }

    @MustBeClosed
    private JsonArrayWriter openJsonArrayWriter() throws IOException {
        beforeOpenContainerWriter();
        return new JsonArrayWriter();
    }

    @MustBeClosed
    private JsonObjectWriter openJsonObjectWriter() throws IOException {
        beforeOpenContainerWriter();
        return new JsonObjectWriter();
    }

    // Style-related methods

    private void beforeContainerChild() throws IOException {
        if (style.isMultiLine()) {
            style.getNewline().write(writer);
            style.getIndentation().writeIndent(writer, nestingDepth);
        }
    }

    private void afterComma() throws IOException {
        if (style.isSpaceAfterComma()) {
            writer.write(' ');
        }
    }

    private void afterColon() throws IOException {
        if (style.isSpaceAfterColon()) {
            writer.write(' ');
        }
    }
}
