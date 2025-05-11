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
import java.util.Locale;
import jserde.core.ser.DataMapWriter;
import jserde.core.ser.DataSequenceWriter;
import jserde.core.ser.DataStructWriter;
import jserde.core.ser.DataValueWriter;
import jserde.core.ser.SerializationException;
import jserde.core.ser.ValueSerializer;
import jserde.io.AbstractWriter;
import jserde.json.JsonFormat;
import org.jspecify.annotations.Nullable;

/**
 * JSON {@link DataValueWriter}.
 *
 * @author Laurent Pireyn
 */
public final class JsonValueWriter implements DataValueWriter {
    public static final JsonStyle DEFAULT_STYLE = JsonStyle.MINIFIED;

    // TODO #improvement: Make max nesting depth configurable
    private static final int MAX_NESTING_DEPTH = 100;

    private final class JsonStringWriter extends AbstractWriter {
        // TODO: Consider using java.util.HexFormat
        private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        void reopen() throws IOException {
            writer.write('"');
        }

        @Override
        public void write(int c) throws IOException {
            switch (c) {
                case '"', '\\' -> {
                    writer.write('\\');
                    writer.write(c);
                }
                case '\n' -> {
                    writer.write('\\');
                    writer.write('n');
                }
                case '\r' -> {
                    writer.write('\\');
                    writer.write('r');
                }
                case '\t' -> {
                    writer.write('\\');
                    writer.write('t');
                }
                case '\f' -> {
                    writer.write('\\');
                    writer.write('f');
                }
                case '\b' -> {
                    writer.write('\\');
                    writer.write('b');
                }
                // NOTE: Slash ('/') is not escaped
                default -> {
                    if (c < ' ') {
                        // Control character
                        writer.write('\\');
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
            writer.write('"');
        }
    }

    private abstract sealed class JsonContainerWriter implements Closeable permits JsonSequenceWriter, JsonStructWriter {
        private boolean closed;

        @MustBeClosed
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
                beforeContainerChild();
                writeContainerEnd();
            }
        }

        @ForOverride
        abstract void writeContainerEnd() throws IOException;
    }

    private final class JsonSequenceWriter extends JsonContainerWriter implements DataSequenceWriter {
        private int index;

        @MustBeClosed
        JsonSequenceWriter() throws IOException {}

        @Override
        void writeContainerBegin() throws IOException {
            writer.write('[');
        }

        @Override
        public <T extends @Nullable Object> void serializeElement(T value, ValueSerializer<? super T> serializer) throws IOException {
            if (index > 0) {
                writer.write(',');
                afterComma();
            }
            beforeContainerChild();
            serializer.serializeValue(value, JsonValueWriter.this);
            ++index;
        }

        @Override
        void writeContainerEnd() throws IOException {
            writer.write(']');
        }
    }

    private final class JsonStructWriter extends JsonContainerWriter implements DataStructWriter {
        private int index;

        @MustBeClosed
        JsonStructWriter() throws IOException {}

        @Override
        void writeContainerBegin() throws IOException {
            writer.write('{');
        }

        @Override
        public <T extends @Nullable Object> void serializeField(String name, T value, ValueSerializer<? super T> serializer) throws IOException {
            if (index > 0) {
                writer.write(',');
                afterComma();
            }
            beforeContainerChild();
            serializeString(name);
            writer.write(':');
            afterColon();
            serializer.serializeValue(value, JsonValueWriter.this);
            ++index;
        }

        @Override
        void writeContainerEnd() throws IOException {
            writer.write('}');
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
     * Current nesting depth.
     */
    private int nestingDepth;

    /**
     * String used for each indent level.
     */
    private final String indentString;

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
    public JsonValueWriter(Writer writer, JsonStyle style) {
        this.writer = writer;
        this.style = style;
        // Prepare indent string based on style
        final var indentStyle = style.getIndentStyle();
        final var indentSize = style.getIndentSize();
        if (indentStyle != IndentStyle.NONE && indentSize > 0) {
            final var ch = indentStyle == IndentStyle.SPACE ? ' ' : '\t';
            indentString = String.valueOf(ch).repeat(indentSize);
        } else {
            indentString = "";
        }
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

    // DataValueWriter methods

    @Override
    public void serializeNull() throws IOException {
        writer.write("null");
    }

    @Override
    public void serializeBoolean(boolean value) throws IOException {
        writer.write(value ? "true" : "false");
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
        try (var writer = openStringWriter()) {
            writer.write(value);
        }
    }

    @Override
    public void serializeString(String value) throws IOException {
        try (var writer = openStringWriter()) {
            writer.write(value);
        }
    }

    @Override
    public void serializeString(Reader reader, int lengthHint) throws IOException {
        try (
            reader;
            var writer = openStringWriter()
        ) {
            reader.transferTo(writer);
        }
    }

    @Override
    public void serializeByteArray(byte[] value) throws IOException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    @MustBeClosed
    public DataSequenceWriter serializeSequence(int size) throws IOException {
        return openSequenceWriter();
    }

    @Override
    @MustBeClosed
    // TODO: Don't suppress MustBeClosedChecker when it supports this flow
    @SuppressWarnings("MustBeClosedChecker")
    public DataMapWriter serializeMap(int size) throws IOException {
        return openStructWriter().asMapWriter();
    }

    @Override
    @MustBeClosed
    public DataStructWriter serializeStruct(int fieldCount) throws IOException {
        return openStructWriter();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    // Child writers-related methods

    @MustBeClosed
    private JsonStringWriter openStringWriter() throws IOException {
        stringWriter.reopen();
        return stringWriter;
    }

    private void beforeOpenContainerWriter() throws SerializationException {
        if (nestingDepth == MAX_NESTING_DEPTH) {
            throw new SerializationException("The maximum nesting depth of " + MAX_NESTING_DEPTH + " has been reached");
        }
        ++nestingDepth;
    }

    private void afterCloseContainerWriter() {
        --nestingDepth;
        assert nestingDepth >= 0;
    }

    @MustBeClosed
    private JsonSequenceWriter openSequenceWriter() throws IOException {
        beforeOpenContainerWriter();
        return new JsonSequenceWriter();
    }

    @MustBeClosed
    private JsonStructWriter openStructWriter() throws IOException {
        beforeOpenContainerWriter();
        return new JsonStructWriter();
    }

    // Style-related methods

    private void beforeContainerChild() throws IOException {
        final var indentStyle = style.getIndentStyle();
        if (indentStyle != IndentStyle.NONE) {
            writer.write('\n');
            for (int i = 0; i < nestingDepth; ++i) {
                writer.write(indentString);
            }
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
