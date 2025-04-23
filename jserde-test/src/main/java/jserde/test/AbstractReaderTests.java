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

package jserde.test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.CharBuffer;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Abstract tests of a {@link Reader}.
 *
 * @author Laurent Pireyn
 */
@SuppressWarnings("checkstyle:MissingJavadocType")
public abstract class AbstractReaderTests extends AbstractTests {
    private final char[] array = new char[10];
    private final CharBuffer buffer = CharBuffer.allocate(10);
    private final StringWriter stringWriter = new StringWriter(10);

    protected abstract Reader createReader(String content);

    @Nested
    public class WithEmptyContent {
        Reader reader = createReader("");

        @Test
        void testReadChar() throws IOException {
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadCharTwice() throws IOException {
            assertEquals(-1, reader.read());
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadArray() throws IOException {
            assertEquals(-1, reader.read(array));
        }

        @Test
        void testReadArrayWithLength0() throws IOException {
            assertEquals(0, reader.read(array, 0, 0));
        }

        @Test
        void testReadBuffer() throws IOException {
            assertEquals(-1, reader.read(buffer));
            assertEquals(0, buffer.position());
        }

        @Test
        void testSkipWith0() throws IOException {
            assertEquals(0L, reader.skip(0L));
        }

        @Test
        void testSkipWith1() throws IOException {
            assertEquals(0L, reader.skip(1L));
        }

        @Test
        void testTransferTo() throws IOException {
            assertEquals(0, reader.transferTo(stringWriter));
            assertEquals("", stringWriter.toString());
        }

        @Test
        void testTransferToAfterEof() throws IOException {
            assertEquals(0, reader.transferTo(stringWriter));
            assertEquals("", stringWriter.toString());
            assertEquals(0, reader.transferTo(stringWriter));
            assertEquals("", stringWriter.toString());
        }

        @Test
        void testClose() {
            assertDoesNotThrow(reader::close);
        }
    }

    @Nested
    public class With1CharContent {
        Reader reader = createReader("a");

        @Test
        void testReadChar() throws IOException {
            assertEquals('a', reader.read());
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadCharAfterEof() throws IOException {
            assertEquals('a', reader.read());
            assertEquals(-1, reader.read());
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadArray() throws IOException {
            assertEquals(1, reader.read(array));
            assertEquals('a', array[0]);
        }

        @Test
        void testReadArrayWithLength0() throws IOException {
            assertEquals(0, reader.read(array, 0, 0));
            assertEquals(0, array[0]);
        }

        @Test
        void testReadArrayWithLength1() throws IOException {
            assertEquals(1, reader.read(array, 0, 1));
            assertEquals('a', array[0]);
        }

        @Test
        void testReadArrayWithLength2() throws IOException {
            assertEquals(1, reader.read(array, 0, 2));
            assertEquals('a', array[0]);
        }

        @Test
        void testReadArrayAfterEof() throws IOException {
            assertEquals(1, reader.read(array));
            assertEquals(-1, reader.read(array));
        }

        @Test
        void testReadBuffer() throws IOException {
            assertEquals(1, reader.read(buffer));
            assertEquals('a', buffer.get(0));
            assertEquals(1, buffer.position());
        }

        @Test
        void testSkipWith0() throws IOException {
            assertEquals(0L, reader.skip(0L));
        }

        @Test
        void testSkipWith1() throws IOException {
            assertEquals(1L, reader.skip(1L));
        }

        @Test
        void testSkipWith2() throws IOException {
            assertEquals(1L, reader.skip(2L));
        }

        @Test
        void testSkipAfterEof() throws IOException {
            assertEquals(1L, reader.skip(1L));
            assertEquals(0L, reader.skip(1L));
        }

        @Test
        void testTransferTo() throws IOException {
            assertEquals(1, reader.transferTo(stringWriter));
            assertEquals("a", stringWriter.toString());
        }

        @Test
        void testTransferToAfterEof() throws IOException {
            assertEquals(1, reader.transferTo(stringWriter));
            assertEquals("a", stringWriter.toString());
            assertEquals(0, reader.transferTo(stringWriter));
            assertEquals("a", stringWriter.toString());
        }

        @Test
        void testClose() {
            assertDoesNotThrow(reader::close);
        }
    }

    @Nested
    public class With3CharsContent {
        Reader reader = createReader("abc");

        @Test
        void testReadChar() throws IOException {
            assertEquals('a', reader.read());
            assertEquals('b', reader.read());
            assertEquals('c', reader.read());
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadCharAfterEof() throws IOException {
            assertEquals('a', reader.read());
            assertEquals('b', reader.read());
            assertEquals('c', reader.read());
            assertEquals(-1, reader.read());
            assertEquals(-1, reader.read());
        }

        @Test
        void testReadArray() throws IOException {
            assertEquals(3, reader.read(array));
            assertEquals('a', array[0]);
            assertEquals('b', array[1]);
            assertEquals('c', array[2]);
        }

        @Test
        void testReadArrayWithLength0() throws IOException {
            assertEquals(0, reader.read(array, 0, 0));
            assertEquals(0, array[0]);
        }

        @Test
        void testReadArrayTwice() throws IOException {
            assertEquals(1, reader.read(array, 0, 1));
            assertEquals('a', array[0]);
            assertEquals(2, reader.read(array, 1, 2));
            assertEquals('a', array[0]);
            assertEquals('b', array[1]);
            assertEquals('c', array[2]);
        }

        @Test
        void testReadArrayAfterEof() throws IOException {
            assertEquals(3, reader.read(array));
            assertEquals(-1, reader.read(array));
        }

        @Test
        void testReadBuffer() throws IOException {
            assertEquals(3, reader.read(buffer));
            assertEquals('a', buffer.get(0));
            assertEquals('b', buffer.get(1));
            assertEquals('c', buffer.get(2));
            assertEquals(3, buffer.position());
        }

        @Test
        void testSkipWith0() throws IOException {
            assertEquals(0L, reader.skip(0L));
        }

        @Test
        void testSkipWith1() throws IOException {
            assertEquals(1L, reader.skip(1L));
            assertEquals('b', reader.read());
        }

        @Test
        void testSkipWith2() throws IOException {
            assertEquals(2L, reader.skip(2L));
            assertEquals('c', reader.read());
        }

        @Test
        void testSkipWith3() throws IOException {
            assertEquals(3L, reader.skip(3L));
            assertEquals(-1, reader.read());
        }

        @Test
        void testSkipWith4() throws IOException {
            assertEquals(3L, reader.skip(4L));
        }

        @Test
        void testSkipAfterEof() throws IOException {
            assertEquals(3L, reader.skip(10L));
            assertEquals(0L, reader.skip(1L));
        }

        @Test
        void testTransferTo() throws IOException {
            assertEquals(3, reader.transferTo(stringWriter));
            assertEquals("abc", stringWriter.toString());
        }

        @Test
        void testTransferToAfterEof() throws IOException {
            assertEquals(3, reader.transferTo(stringWriter));
            assertEquals("abc", stringWriter.toString());
            assertEquals(0, reader.transferTo(stringWriter));
            assertEquals("abc", stringWriter.toString());
        }

        @Test
        void testClose() {
            assertDoesNotThrow(reader::close);
        }
    }
}
