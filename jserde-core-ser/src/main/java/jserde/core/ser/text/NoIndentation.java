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

import java.io.Writer;

/**
 * {@link Indentation} that has an empty indent.
 *
 * @author Laurent Pireyn
 */
final class NoIndentation extends Indentation {
    static final NoIndentation INSTANCE = new NoIndentation();

    private NoIndentation() {
    }

    @Override
    public String getIndent() {
        return "";
    }

    @Override
    public void writeIndent(Writer writer) {
        // Do nothing
    }

    @Override
    public void writeIndent(Writer writer, int count) {
        // Do nothing
    }
}
