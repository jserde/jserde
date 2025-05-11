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

package jserde.toml.de.service;

import java.util.Set;
import jserde.core.de.factory.BinaryValueReaderFactory;
import jserde.core.de.factory.TextValueReaderFactory;
import jserde.core.de.service.DataValueReaderService;
import jserde.toml.TomlFormat;
import jserde.toml.de.TomlValueReaderFactory;

/**
 * JSON {@link DataValueReaderService}.
 *
 * @author Laurent Pireyn
 */
public final class TomlValueReaderService implements DataValueReaderService {
    @Override
    public String getFormat() {
        return TomlFormat.NAME;
    }

    @Override
    public Set<String> getSupportedMediaTypes() {
        return Set.of(
            TomlFormat.MEDIA_TYPE,
            // Alternative, commonly used media type
            "text/toml"
        );
    }

    @Override
    public Set<String> getSupportedFileExtensions() {
        return Set.of(TomlFormat.FILE_EXTENSION);
    }

    @Override
    public BinaryValueReaderFactory getBinaryValueReaderFactory() {
        return TomlValueReaderFactory.INSTANCE;
    }

    @Override
    public TextValueReaderFactory getTextValueReaderFactory() {
        return TomlValueReaderFactory.INSTANCE;
    }
}
