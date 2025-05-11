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

package jserde.json.ser;

import java.io.OutputStream;
import java.io.Writer;
import jserde.core.ser.factory.BinaryValueWriterFactory;
import jserde.core.ser.factory.TextValueWriterFactory;

/**
 * Factory of {@link JsonValueWriter}s.
 *
 * @author Laurent Pireyn
 */
public final class JsonValueWriterFactory implements TextValueWriterFactory, BinaryValueWriterFactory {
    /**
     * Singleton instance of this class.
     */
    public static final JsonValueWriterFactory INSTANCE = new JsonValueWriterFactory();

    private JsonValueWriterFactory() {}

    @Override
    public JsonValueWriter createDataValueWriter(OutputStream output) {
        return new JsonValueWriter(output);
    }

    @Override
    public JsonValueWriter createDataValueWriter(Writer writer) {
        return new JsonValueWriter(writer);
    }
}
