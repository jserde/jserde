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

package jserde.json.ser.service;

import java.util.Set;
import jserde.core.ser.factory.BinaryValueWriterFactory;
import jserde.core.ser.factory.TextValueWriterFactory;
import jserde.core.ser.service.DataValueWriterService;
import jserde.json.JsonFormat;
import jserde.json.ser.JsonValueWriterFactory;

/**
 * JSON {@link DataValueWriterService}.
 *
 * @author Laurent Pireyn
 */
public final class JsonValueWriterService implements DataValueWriterService {
    @Override
    public String getFormat() {
        return JsonFormat.NAME;
    }

    @Override
    public Set<String> getSupportedMediaTypes() {
        return Set.of(
            JsonFormat.MEDIA_TYPE,
            // Alternative, commonly used media type
            "text/json"
        );
    }

    @Override
    public Set<String> getSupportedFileExtensions() {
        return Set.of(JsonFormat.FILE_EXTENSION);
    }

    @Override
    public BinaryValueWriterFactory getBinaryValueWriterFactory() {
        return JsonValueWriterFactory.INSTANCE;
    }

    @Override
    public TextValueWriterFactory getTextValueWriterFactory() {
        return JsonValueWriterFactory.INSTANCE;
    }
}
