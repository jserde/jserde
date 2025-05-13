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

package jserde.core.ser.text;

import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.io.Writer;

/**
 * Indentation used by some text formats.
 *
 * @author Laurent Pireyn
 */
@Immutable
public abstract sealed class Indentation permits NoIndentation, IndentWithSpaces, IndentWithTab {
    /**
     * Returns an indentation with an empty indent.
     *
     * @return an indentation with an empty indent
     */
    public static Indentation none() {
        return NoIndentation.INSTANCE;
    }

    /**
     * Returns an indentation with an indent made of one or more space characters.
     *
     * <p>If the number of space characters is zero (or negative), the indentation is equivalent to {@link #none()}.
     *
     * @param count the number of space characters in an indent
     * @return an indentation with an indent made of {@code count} space character(s)
     */
    public static Indentation spaces(int count) {
        return count > 0 ? new IndentWithSpaces(count) : none();
    }

    /**
     * Returns an indentation with an indent made of a tab character.
     *
     * @return an indentation with an indent made of a tab character
     */
    public static Indentation tab() {
        return IndentWithTab.INSTANCE;
    }

    /**
     * Returns the indent of this indentation.
     *
     * @return the indent of this indentation
     */
    public abstract String getIndent();

    /**
     * Writes the indent of this indentation to the given {@link Writer}.
     *
     * @param writer the writer
     * @throws IOException thrown by {@code writer}
     */
    public abstract void writeIndent(Writer writer) throws IOException;

    /**
     * Writes the given number of indents of this indentation to the given {@link Writer}.
     *
     * @param writer the writer
     * @param count the number of indents to write
     * @throws IOException thrown by {@code writer}
     */
    public void writeIndent(Writer writer, int count) throws IOException {
        for (int i = 0; i < count; ++i) {
            writeIndent(writer);
        }
    }
}
