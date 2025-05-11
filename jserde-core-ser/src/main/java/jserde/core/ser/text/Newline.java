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
 * Newline used by some text formats.
 *
 * @author Laurent Pireyn
 */
@Immutable
public abstract sealed class Newline permits Lf, CrLf {
    /**
     * Returns a newline made of the LF character.
     *
     * @return a newline made of the LF character
     */
    public static Newline lf() {
        return Lf.INSTANCE;
    }

    /**
     * Returns a newline made of the CR, LF sequence of characters.
     *
     * @return a newline made of the CR, LF sequence of characters
     */
    public static Newline crLf() {
        return CrLf.INSTANCE;
    }

    /**
     * Returns this newline as a string.
     *
     * @return this newline as a string
     */
    public abstract String getAsString();

    /**
     * Writes this newline to the given {@link Writer}.
     *
     * @param writer the writer
     * @throws IOException thrown by {@code writer}
     */
    public abstract void write(Writer writer) throws IOException;
}
