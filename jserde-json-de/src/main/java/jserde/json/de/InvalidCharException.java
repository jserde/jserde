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

import java.io.Serial;
import java.util.HexFormat;
import jserde.core.de.InvalidSyntaxException;

/**
 * {@link InvalidSyntaxException} thrown when an invalid character is read.
 *
 * @author Laurent Pireyn
 */
// TODO #design: Consider moving this class to jserde-core-de
public class InvalidCharException extends InvalidSyntaxException {
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    @Serial private static final long serialVersionUID = 1L;

    private static String createMessage(int invalidChar, int line, int column, String expected) {
        final var message = new StringBuilder(100);
        message.append("Invalid ").append(charDescription(invalidChar));
        if (line != -1) {
            assert column != -1;
            message.append(" at line ").append(line)
                .append(", column ").append(column);
        }
        message.append("; expected ").append(expected);
        return message.toString();
    }

    private static String charDescription(int c) {
        return c >= 0
            ? "character '" + escaped((char) c) + "' (" + Integer.toUnsignedString(c) + ')'
            : "EOF";
    }

    private static String escaped(char c) {
        return switch (c) {
            case '\\' -> "\\\\";
            case '\'' -> "\\'";
            case '\b' -> "\\b";
            case '\t' -> "\\t";
            case '\n' -> "\\n";
            case '\f' -> "\\f";
            case '\r' -> "\\r";
            default -> c < ' '
                ? "\\u" + HEX_FORMAT.toHexDigits(c)
                : String.valueOf(c);
        };
    }

    private final int invalidChar;
    private final int line;
    private final int column;
    private final String expected;

    InvalidCharException(int invalidChar, int line, int column, String expected) {
        super(createMessage(invalidChar, line, column, expected));
        this.invalidChar = invalidChar;
        this.line = line;
        this.column = column;
        this.expected = expected;
    }

    public final int getInvalidChar() {
        return invalidChar;
    }

    public final int getLine() {
        return line;
    }

    public final int getColumn() {
        return column;
    }

    public final String getExpected() {
        return expected;
    }
}
