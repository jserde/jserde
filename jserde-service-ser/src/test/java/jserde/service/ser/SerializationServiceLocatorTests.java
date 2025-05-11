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

package jserde.service.ser;

import jserde.json.JsonFormat;
import jserde.test.AbstractTests;
import jserde.toml.TomlFormat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SerializationServiceLocatorTests extends AbstractTests {
    @ParameterizedTest
    @ValueSource(strings = {
        JsonFormat.NAME,
        TomlFormat.NAME,
    })
    void testFindDataValueWriterServiceByFormat(String format) {
        assertNotNull(SerializationServiceLocator.findDataValueWriterServiceByFormat(format));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        JsonFormat.MEDIA_TYPE,
        TomlFormat.MEDIA_TYPE,
    })
    void testFindDataValueWriterServiceByMediaType(String mediaType) {
        assertNotNull(SerializationServiceLocator.findDataValueWriterServiceByMediaType(mediaType));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        JsonFormat.FILE_EXTENSION,
        TomlFormat.FILE_EXTENSION,
    })
    void testFindDataValueWriterServiceByFileExtension(String fileExtension) {
        assertNotNull(SerializationServiceLocator.findDataValueWriterServiceByFileExtension(fileExtension));
    }
}
