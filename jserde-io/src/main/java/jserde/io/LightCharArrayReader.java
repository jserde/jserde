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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * {@link Reader} on a {@code char} array.
 *
 * <p>This class is a lighter and faster alternative to {@link CharArrayReader}.
 *
 * <p>Improvements:
 *
 * <ul>
 *     <li>The {@link #close()} method moves this reader to EOF rather than invalidating it,
 *     which means the {@code read} methods will not throw an {@link IOException}.
 *     <li>The arguments are not validated.
 *     <li>No synchronization, which means this class is not thread-safe.
 * </ul>
 *
 * @author Laurent Pireyn
 */
public final class LightCharArrayReader extends AbstractReader {
    private final char[] array;
    private final int arrayLength;
    private int index;
    private int mark;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public LightCharArrayReader(char[] array) {
        this.array = array;
        arrayLength = array.length;
    }

    public int remaining() {
        return arrayLength - index;
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public int read() {
        final var index = this.index;
        if (index == arrayLength) {
            return -1;
        }
        this.index = index + 1;
        return array[index];
    }

    // Overridden to remove throws clause
    @Override
    public int read(char[] array) {
        return read(array, 0, array.length);
    }

    @Override
    public int read(char[] array, int offset, int length) {
        if (length == 0) {
            return 0;
        }
        final var remaining = remaining();
        if (remaining == 0) {
            return -1;
        }
        final var count = Math.min(length, remaining);
        assert count > 0;
        System.arraycopy(this.array, index, array, offset, count);
        index += count;
        return count;
    }

    @Override
    public int read(CharBuffer buffer) {
        final var remaining = remaining();
        if (remaining == 0) {
            return -1;
        }
        final int count = Math.min(buffer.remaining(), remaining);
        if (count > 0) {
            if (buffer.hasArray()) {
                final var bufferPosition = buffer.position();
                System.arraycopy(array, index, buffer.array(), buffer.arrayOffset() + bufferPosition, count);
                buffer.position(bufferPosition + count);
            } else {
                buffer.put(array, index, count);
            }
            index += count;
        }
        return count;
    }

    @Override
    public long skip(long count) {
        final var actualCount = (int) Math.min(count, arrayLength - index);
        index += actualCount;
        return actualCount;
    }

    @Override
    public long transferTo(Writer writer) throws IOException {
        final var count = arrayLength - index;
        writer.write(array, index, count);
        index += count;
        return count;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) {
        mark = index;
    }

    @Override
    public void reset() {
        index = mark;
    }

    @Override
    public void close() {
        index = arrayLength;
    }
}
