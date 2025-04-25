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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import jserde.core.de.DeserializationException;
import jserde.core.de.ValueDiscarder;
import jserde.core.de.standard.StandardObjectDeserializer;
import jserde.test.AbstractTests;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.support.Resource;

/**
 * Tests that run the <a href="https://github.com/nst/JSONTestSuite">JSONTestSuite</a>.
 *
 * <p>The JSON files used by these tests are a copy of the
 * <a href="https://github.com/nst/JSONTestSuite/commit/1ef36fa01286573e846ac449e8683f8833c5b26a">{@code master} branch on 2024-11-22</a>.
 *
 * @author Laurent Pireyn
 * @author Nicolas Seriot (JSONTestSuite)
 * @see <a href="https://seriot.ch/projects/parsing_json.html">Parsing JSON is a Minefield</a>
 */
@DisplayName("JSONTestSuite")
class JsonTestSuiteTests extends AbstractTests {
    static final Charset[] CHARSETS = {
        StandardCharsets.UTF_8,
        StandardCharsets.UTF_16,
    };

    static String fileName(String name) {
        final var index = name.lastIndexOf('/');
        return index != -1
            ? name.substring(index + 1)
            : name;
    }

    static String displayName(String name) {
        final var fileName = fileName(name);
        // Strip two-letter prefix and ".json" extension, and replace underscores with spaces
        return fileName.substring(2, fileName.length() - ".json".length())
            .replace('_', ' ');
    }

    static Stream<Arguments> dataJsonFiles(String prefix) {
        return ReflectionSupport.streamAllResourcesInPackage(
            "json-test-suite",
            resource -> {
                final var name = fileName(resource.getName());
                return name.endsWith(".json") && name.startsWith(prefix);
            }
        )
            .map(resource -> arguments(displayName(resource.getName()), resource));
    }

    void parseJson(Resource resource) throws IOException {
        parseJson(resource, StandardCharsets.UTF_8);
    }

    void parseJson(Resource resource, Charset charset) throws IOException {
        final String input;
        try (
            var stringWriter = new StringWriter();
            var reader = new InputStreamReader(resource.getInputStream(), charset)
        ) {
            reader.transferTo(stringWriter);
            input = stringWriter.toString();
        }
        log("Input: " + input);
        final var reader = new JsonValueReader(input);
        final var value = StandardObjectDeserializer.INSTANCE.deserializeValue(reader);
        log("Deserialized value: " + value);
        try {
            ValueDiscarder.INSTANCE.deserializeValue(reader);
            // Treat a second JSON value as a deserialization error here
            throw new DeserializationException("Unexpected value; expected EOF");
        } catch (EOFException e) {
            // Expected EOF
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void testValidJson(String displayName, Resource resource) {
        assertDoesNotThrow(() -> parseJson(resource));
    }

    static Stream<Arguments> testValidJson() {
        return dataJsonFiles("y_");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void testInvalidJson(String displayName, Resource resource) {
        final var exception = assertThrows(IOException.class, () -> parseJson(resource));
        logExpectedException(exception);
    }

    static Stream<Arguments> testInvalidJson() {
        return dataJsonFiles("n_");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void testUndefinedJson(String displayName, Resource resource) throws IOException {
        for (final var charset : CHARSETS) {
            log("Charset: " + charset);
            try {
                parseJson(resource, charset);
                break;
            } catch (DeserializationException e) {
                log("Allowed " + e.getClass().getSimpleName(), e);
            }
        }
    }

    static Stream<Arguments> testUndefinedJson() {
        return dataJsonFiles("i_");
    }
}
