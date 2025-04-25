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
import jserde.core.de.InvalidValueException;

/**
 * {@link InvalidValueException} thrown when an invalid JSON value is read.
 *
 * @author Laurent Pireyn
 */
public class InvalidJsonValueException extends InvalidValueException {
    @Serial private static final long serialVersionUID = 1L;

    private static String createMessage(String invalidValue, int line, int column, String expected) {
        final var message = new StringBuilder(100);
        message.append("Invalid ").append(invalidValue);
        if (line != -1) {
            assert column != -1;
            message.append(" at line ").append(line)
                .append(", column ").append(column);
        }
        message.append("; expected ").append(expected);
        return message.toString();
    }

    private final String invalidValue;
    private final int line;
    private final int column;
    private final String expected;

    InvalidJsonValueException(String invalidValue, int line, int column, String expected) {
        super(createMessage(invalidValue, line, column, expected));
        this.invalidValue = invalidValue;
        this.line = line;
        this.column = column;
        this.expected = expected;
    }

    public final String getInvalidValue() {
        return invalidValue;
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
