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
        IndentStyle indentStyle = IndentStyle.NONE;
        int indentSize = 2;
        boolean spaceAfterComma;
        boolean spaceAfterColon;

        Builder() {}

        public Builder indentStyle(IndentStyle indentStyle) {
            this.indentStyle = indentStyle;
            return this;
        }

        public Builder indentSize(int indentSize) {
            if (indentSize < 0) {
                throw new IllegalArgumentException("Negative indent size: " + indentSize);
            }
            this.indentSize = indentSize;
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
     * <p>The resulting JSON text is indented with 2 spaces and has a space after commas and colons.
     */
    public static final JsonStyle PRETTY = builder()
        .indentStyle(IndentStyle.SPACE)
        .spaceAfterComma(true)
        .spaceAfterColon(true)
        .build();

    /**
     * Creates a {@link Builder}.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private final IndentStyle indentStyle;
    private final int indentSize;
    private final boolean spaceAfterComma;
    private final boolean spaceAfterColon;

    private JsonStyle(Builder builder) {
        indentStyle = builder.indentStyle;
        indentSize = builder.indentSize;
        spaceAfterComma = builder.spaceAfterComma;
        spaceAfterColon = builder.spaceAfterColon;
    }

    public IndentStyle getIndentStyle() {
        return indentStyle;
    }

    public int getIndentSize() {
        return indentSize;
    }

    public boolean isSpaceAfterComma() {
        return spaceAfterComma;
    }

    public boolean isSpaceAfterColon() {
        return spaceAfterColon;
    }
}
