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

import java.io.InputStream;
import java.io.Reader;
import jserde.core.de.factory.BinaryValueReaderFactory;
import jserde.core.de.factory.TextValueReaderFactory;

/**
 * Factory of {@link JsonValueReader}s.
 *
 * @author Laurent Pireyn
 */
public final class JsonValueReaderFactory implements TextValueReaderFactory, BinaryValueReaderFactory {
    /**
     * Singleton instance of this class.
     */
    public static final JsonValueReaderFactory INSTANCE = new JsonValueReaderFactory();

    private JsonValueReaderFactory() {}

    @Override
    public JsonValueReader createDataValueReader(InputStream input) {
        return new JsonValueReader(input);
    }

    @Override
    public JsonValueReader createDataValueReader(Reader reader) {
        return new JsonValueReader(reader);
    }
}
