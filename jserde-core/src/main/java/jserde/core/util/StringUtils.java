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

package jserde.core.util;

import java.util.HexFormat;

/**
 * String utilities.
 *
 * @author Laurent Pireyn
 */
public final class StringUtils {
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    public static String javaEscaped(char c) {
        return switch (c) {
            // Ordered by value
            case '\b' -> "\\b";
            case '\t' -> "\\t";
            case '\n' -> "\\n";
            case '\f' -> "\\f";
            case '\r' -> "\\r";
            case '"' -> "\\\"";
            case '\'' -> "\\'";
            case '\\' -> "\\\\";
            default -> Character.isISOControl(c) ? javaUnicodeEscaped(c) : String.valueOf(c);
        };
    }

    public static String javaEscaped(CharSequence sequence) {
        final var length = sequence.length();
        if (length == 0) {
            return "";
        }
        final var builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            builder.append(javaEscaped(sequence.charAt(i)));
        }
        return builder.toString();
    }

    public static String javaUnicodeEscaped(char c) {
        return "\\u" + HEX_FORMAT.toHexDigits(c);
    }

    public static String charDescription(char c) {
        return "'" + javaEscaped(c) + "' (" + Integer.toUnsignedString(c) + ')';
    }

    public static String charDescription(int c) {
        return c >= 0 ? charDescription((char) c) : "EOF";
    }

    private StringUtils() {}
}
