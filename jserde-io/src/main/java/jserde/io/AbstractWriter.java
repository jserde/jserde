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

package jserde.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Abstract {@link Writer}.
 *
 * <p>This class is more convenient to extend than {@code Writer},
 * and is potentially slightly more performant.
 *
 * <p>Improvements:
 *
 * <ul>
 *     <li>The {@link #write(int)} method is abstract,
 *     and the other {@code write} methods invoke it.
 *     <li>The {@link #flush()} and {@link #close()} methods are implemented and do nothing.
 *     <li>No synchronization, which means this class is not thread-safe.
 * </ul>
 *
 * @author Laurent Pireyn
 */
public abstract class AbstractWriter extends Writer {
    @Override
    public abstract void write(int c) throws IOException;

    @Override
    public void write(char[] array, int offset, int length) throws IOException {
        for (int i = 0; i < length; ++i) {
            write(array[offset + i]);
        }
    }

    @Override
    public void write(String string, int offset, int length) throws IOException {
        append(string, offset, offset + length);
    }

    // NOTE: Effectively final
    @Override
    public Writer append(CharSequence string) throws IOException {
        return append(string, 0, string.length());
    }

    @Override
    public Writer append(CharSequence sequence, int start, int end) throws IOException {
        for (int i = start; i < end; ++i) {
            append(sequence.charAt(i));
        }
        return this;
    }

    @Override
    public void flush() throws IOException {
        // Do nothing
    }

    @Override
    public void close() throws IOException {
        // Do nothing
    }
}
