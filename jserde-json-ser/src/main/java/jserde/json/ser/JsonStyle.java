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

package jserde.json.ser;

import com.google.errorprone.annotations.Immutable;
import jserde.core.ser.text.Indentation;

/**
 * JSON style.
 *
 * @author Laurent Pireyn
 */
@Immutable
public final class JsonStyle {
    /**
     * {@link JsonStyle} builder.
     *
     * @see JsonStyle#builder()
     */
    public static final class Builder {
        boolean multiLine;
        Indentation indentation = Indentation.none();
        boolean spaceAfterComma;
        boolean spaceAfterColon;
        boolean collapseEmptyContainers;

        Builder() {}

        public Builder multiLine(boolean multiLine) {
            this.multiLine = multiLine;
            return this;
        }

        public Builder indentation(Indentation indentation) {
            this.indentation = indentation;
            multiLine = true;
            return this;
        }

        public Builder spaceAfterComma(boolean spaceAfterComma) {
            this.spaceAfterComma = spaceAfterComma;
            return this;
        }

        public Builder spaceAfterColon(boolean spaceAfterColon) {
            this.spaceAfterColon = spaceAfterColon;
            return this;
        }

        public Builder collapseEmptyContainers(boolean collapseEmptyContainers) {
            this.collapseEmptyContainers = collapseEmptyContainers;
            return this;
        }

        public JsonStyle build() {
            return new JsonStyle(this);
        }
    }

    /**
     * JSON style that results in "minified" JSON.
     *
     * <p>The resulting JSON text has no unnecessary white spaces.
     */
    public static final JsonStyle MINIFIED = builder().build();

    /**
     * JSON style that results in "pretty" JSON.
     *
     * <p>The resulting JSON text has multiple lines indented with 2 spaces,
     * has a space after commas and colons,
     * and collapses empty containers.
     */
    public static final JsonStyle PRETTY = builder()
        .indentation(Indentation.spaces(2))
        .spaceAfterComma(true)
        .spaceAfterColon(true)
        .collapseEmptyContainers(true)
        .build();

    /**
     * Creates a {@link Builder}.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private final boolean multiLine;
    private final Indentation indentation;
    private final boolean spaceAfterComma;
    private final boolean spaceAfterColon;
    private final boolean collapseEmptyContainers;

    private JsonStyle(Builder builder) {
        multiLine = builder.multiLine;
        indentation = builder.indentation;
        spaceAfterComma = builder.spaceAfterComma;
        spaceAfterColon = builder.spaceAfterColon;
        collapseEmptyContainers = builder.collapseEmptyContainers;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public Indentation getIndentation() {
        return indentation;
    }

    public boolean isSpaceAfterComma() {
        return spaceAfterComma;
    }

    public boolean isSpaceAfterColon() {
        return spaceAfterColon;
    }

    public boolean isCollapseEmptyContainers() {
        return collapseEmptyContainers;
    }
}
