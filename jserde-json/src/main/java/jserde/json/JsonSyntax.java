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

package jserde.json;

import com.google.errorprone.annotations.Immutable;

/**
 * Constants related to the JSON syntax.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonSyntax {
    // Keywords (ordered by name)
    public static final String FALSE = "false";
    public static final String NULL = "null";
    public static final String TRUE = "true";

    // Symbols (ordered by name)
    public static final char BACKSLASH = '\\';
    public static final char COLON = ':';
    public static final char COMMA = ',';
    public static final char LCB = '{';
    public static final char LSB = '[';
    public static final char QUOTATION_MARK = '"';
    public static final char RCB = '}';
    public static final char RSB = ']';

    // Whitespaces (ordered by value)
    public static final char BS = '\b';
    public static final char HT = '\t';
    public static final char FF = '\f';
    public static final char LF = '\n';
    public static final char CR = '\r';
    public static final char SP = ' ';

    private JsonSyntax() {}
}
