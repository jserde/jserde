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

package jserde.json.model;

import jserde.test.AbstractTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JsonValueTests extends AbstractTests {
    @ParameterizedTest
    @ValueSource(strings = {
        "null",
        "true",
        "false",
        "0",
        "1",
        "-1",
        "0.5",
        "-0.5",
        "12345678901234567890123456789012345678901234567890",
        "-12345678901234567890123456789012345678901234567890",
        "12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890",
        "-12345678901234567890123456789012345678901234567890.12345678901234567890123456789012345678901234567890",
        "\"\"",
        "\"a\"",
        "\"abc\"",
        "[]",
        "[null,true,1,\"a\",[],{}]",
        "{}",
        "{\"a\":true,\"b\":1,\"c\":\"x\",\"d\":[],\"e\":{}}",
    })
    void testSerDe(String json) {
        log("JSON: " + json);
        final var value = JsonValue.parseJsonValue(json);
        log("JSON value: " + value + " (class: " + value.getClass().getSimpleName() + ')');
        assertEquals(json, value.toJson());
    }
}
