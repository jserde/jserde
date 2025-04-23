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
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * Abstract {@link Reader}.
 *
 * <p>This class is more convenient to extend than {@code Reader}.
 *
 * @author Laurent Pireyn
 */
public abstract class AbstractReader extends Reader {
    @Override
    public abstract int read() throws IOException;

    @Override
    public int read(char[] array, int offset, int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        int count = 0;
        while (count < length) {
            final var c = read();
            if (c == -1) {
                // End of string
                if (count == 0) {
                    count = -1;
                }
                break;
            }
            array[offset + count] = (char) c;
            ++count;
        }
        return count;
    }

    @Override
    public int read(CharBuffer buffer) throws IOException {
        if (buffer.hasArray()) {
            final var position = buffer.position();
            final var count = read(buffer.array(), buffer.arrayOffset() + position, buffer.remaining());
            if (count > 0) {
                buffer.position(position + count);
            }
            return count;
        }
        int count = 0;
        while (buffer.hasRemaining()) {
            final var c = read();
            if (c == -1) {
                break;
            }
            buffer.put((char) c);
            ++count;
        }
        return count;
    }

    @Override
    public long skip(long count) throws IOException {
        int actualCount = 0;
        while (actualCount < count) {
            final var c = read();
            if (c == -1) {
                break;
            }
            ++actualCount;
        }
        return actualCount;
    }

    @Override
    public long transferTo(Writer writer) throws IOException {
        long count = 0L;
        while (true) {
            final var c = read();
            if (c == -1) {
                // EOF
                break;
            }
            writer.write((char) c);
            ++count;
        }
        return count;
    }

    @Override
    public void close() throws IOException {
        // Do nothing
    }
}
