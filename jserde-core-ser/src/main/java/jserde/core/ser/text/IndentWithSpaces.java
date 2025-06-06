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

package jserde.core.ser.text;

import java.io.IOException;
import java.io.Writer;

/**
 * {@link Indentation} that uses one or more space characters as indent.
 *
 * @author Laurent Pireyn
 */
final class IndentWithSpaces extends Indentation {
    private final String indent;

    IndentWithSpaces(int count) {
        assert count > 0;
        indent = " ".repeat(count);
    }

    @Override
    public String getIndent() {
        return indent;
    }

    @Override
    public void writeIndent(Writer writer) throws IOException {
        writer.write(indent);
    }
}
