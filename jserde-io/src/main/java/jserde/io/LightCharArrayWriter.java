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

import java.io.CharArrayWriter;
import java.io.Writer;

/**
 * {@link Writer} on a growable {@code char} array.
 *
 * <p>This class is a lighter and faster alternative to {@link CharArrayWriter}.
 *
 * <p>Improvements:
 *
 * <ul>
 *     <li>The arguments are not validated.
 *     <li>No synchronization, which means this class is not thread-safe.
 * </ul>
 *
 * @author Laurent Pireyn
 */
public final class LightCharArrayWriter extends AbstractWriter {
    public static final int DEFAULT_INITIAL_CAPACITY = 1024;
    public static final int DEFAULT_CAPACITY_INCREMENT = 1024;

    private char[] buffer;
    private int capacity;
    private final int capacityIncrement;
    private int index;

    public LightCharArrayWriter() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public LightCharArrayWriter(int initialCapacity) {
        this(initialCapacity, Math.min(initialCapacity, DEFAULT_CAPACITY_INCREMENT));
    }

    public LightCharArrayWriter(int initialCapacity, int capacityIncrement) {
        buffer = new char[initialCapacity];
        capacity = buffer.length;
        this.capacityIncrement = capacityIncrement;
    }

    private char[] ensureCapacity(int size) {
        final var capacity = this.capacity;
        final var index = this.index;
        final var available = capacity - index;
        final var missing = size - available;
        if (missing <= 0) {
            return buffer;
        }
        final var increment = Math.max(missing, capacityIncrement);
        final var newCapacity = capacity + increment;
        final var newBuffer = new char[newCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, index);
        buffer = newBuffer;
        this.capacity = newCapacity;
        return newBuffer;
    }

    @Override
    public void write(int c) {
        final var buffer = ensureCapacity(1);
        buffer[index++] = (char) c;
    }

    // Overridden to remove throws clause
    @Override
    public void write(char[] array) {
        write(array, 0, array.length);
    }

    @Override
    public void write(char[] array, int offset, int length) {
        final var buffer = ensureCapacity(length);
        System.arraycopy(array, offset, buffer, index, length);
        index += length;
    }

    // Overridden to remove throws clause
    @Override
    public void write(String string) {
        write(string, 0, string.length());
    }

    @Override
    public void write(String string, int offset, int length) {
        final var array = ensureCapacity(length);
        string.getChars(offset, offset + length, array, index);
        index += length;
    }

    // Overridden to change return type and remove throws clause
    @Override
    public LightCharArrayWriter append(char c) {
        write(c);
        return this;
    }

    // Overridden to change return type and remove throws clause
    @Override
    public LightCharArrayWriter append(CharSequence sequence) {
        return append(sequence, 0, sequence.length());
    }

    @Override
    public LightCharArrayWriter append(CharSequence sequence, int start, int end) {
        if (sequence instanceof String) {
            write((String) sequence, start, end - start);
        } else {
            for (int i = start; i < end; ++i) {
                append(sequence.charAt(i));
            }
        }
        return this;
    }

    // Overridden to remove throws clause
    @Override
    public void flush() {
        // Do nothing
    }

    // Overridden to remove throws clause
    @Override
    public void close() {
        // Do nothing
    }

    public void reset() {
        index = 0;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, index);
    }
}
