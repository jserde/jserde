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

package jserde.core.ser.factory;

import java.io.IOException;
import java.io.OutputStream;
import jserde.core.ser.DataValueWriter;

/**
 * Factory of {@link DataValueWriter}s for a binary format.
 *
 * @author Laurent Pireyn
 */
public interface BinaryValueWriterFactory {
    /**
     * Creates a {@link DataValueWriter} on the given {@link OutputStream}.
     *
     * <p>Closing the returned value writer should close the output stream.
     *
     * @param output the output stream
     * @return a value writer on {@code output}
     * @throws IOException if an I/O error occurs
     */
    // NOTE: This is not annotated with @MustBeClosed, as not all OutputStream subclasses require to be closed
    DataValueWriter createDataValueWriter(OutputStream output) throws IOException;
}
