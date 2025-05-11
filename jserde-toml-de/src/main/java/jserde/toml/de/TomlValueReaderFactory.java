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

package jserde.toml.de;

import java.io.InputStream;
import java.io.Reader;
import jserde.core.de.factory.BinaryValueReaderFactory;
import jserde.core.de.factory.TextValueReaderFactory;

/**
 * Factory of {@link TomlValueReader}s.
 *
 * @author Laurent Pireyn
 */
public final class TomlValueReaderFactory implements TextValueReaderFactory, BinaryValueReaderFactory {
    /**
     * Singleton instance of this class.
     */
    public static final TomlValueReaderFactory INSTANCE = new TomlValueReaderFactory();

    private TomlValueReaderFactory() {}

    @Override
    public TomlValueReader createDataValueReader(InputStream input) {
        return new TomlValueReader(input);
    }

    @Override
    public TomlValueReader createDataValueReader(Reader reader) {
        return new TomlValueReader(reader);
    }
}
