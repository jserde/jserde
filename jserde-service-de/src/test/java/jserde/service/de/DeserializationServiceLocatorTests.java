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

package jserde.service.de;

import jserde.json.JsonFormat;
import jserde.test.AbstractTests;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class DeserializationServiceLocatorTests extends AbstractTests {
    @Test
    void testWithJson() {
        assertAll(() -> {
            assertNotNull(DeserializationServiceLocator.findDataValueReaderServiceByFormat(JsonFormat.NAME));
            assertNotNull(DeserializationServiceLocator.findDataValueReaderServiceByMediaType(JsonFormat.MEDIA_TYPE));
            assertNotNull(DeserializationServiceLocator.findDataValueReaderServiceByFileExtension(JsonFormat.FILE_EXTENSION));
        });
    }
}
