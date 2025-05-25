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

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import jserde.core.ser.DataValueWriter;
import jserde.json.JsonValueType;
import org.jspecify.annotations.Nullable;

/**
 * JSON object.
 *
 * <p>Implementation: this class is backed by a {@link LinkedHashMap}.
 * The order of insertion is therefore preserved.
 *
 * @author Laurent Pireyn
 */
public final class JsonObject extends JsonValue implements Map<String, JsonValue> {
    public static final int DEFAULT_INITIAL_CAPACITY = 10;

    private final Map<String, JsonValue> map;

    private JsonObject(Map<String, JsonValue> map, boolean priv) {
        this.map = map;
    }

    public JsonObject() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public JsonObject(int initialCapacity) {
        this(new LinkedHashMap<>(initialCapacity), true);
    }

    public JsonObject(Map<String, ? extends JsonValue> map) {
        this(new LinkedHashMap<>(map), true);
    }

    /**
     * Returns {@link JsonValueType#OBJECT}.
     */
    @Override
    public JsonValueType getType() {
        return JsonValueType.OBJECT;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return map.containsValue(value);
    }

    @Override
    public @Nullable JsonValue get(@Nullable Object key) {
        return map.get(key);
    }

    @Override
    public JsonValue getOrDefault(@Nullable Object key, JsonValue defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public @Nullable JsonValue put(String key, JsonValue value) {
        return map.put(key, value);
    }

    @Override
    public @Nullable JsonValue putIfAbsent(String key, JsonValue value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonValue> map) {
        this.map.putAll(map);
    }

    @Override
    public @Nullable JsonValue computeIfAbsent(String key, Function<? super String, ? extends @Nullable JsonValue> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public @Nullable JsonValue computeIfPresent(String key, BiFunction<? super String, ? super JsonValue, ? extends @Nullable JsonValue> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public @Nullable JsonValue compute(String key, BiFunction<? super String, ? super @Nullable JsonValue, ? extends @Nullable JsonValue> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public @Nullable JsonValue merge(String key, JsonValue value, BiFunction<? super JsonValue, ? super JsonValue, ? extends @Nullable JsonValue> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }

    @Override
    public @Nullable JsonValue remove(@Nullable Object key) {
        return map.remove(key);
    }

    @Override
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        return map.remove(key, value);
    }

    @Override
    public @Nullable JsonValue replace(String key, JsonValue value) {
        return map.replace(key, value);
    }

    @Override
    public boolean replace(String key, JsonValue oldValue, JsonValue newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super JsonValue, ? extends JsonValue> function) {
        map.replaceAll(function);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super JsonValue> action) {
        map.forEach(action);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<JsonValue> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return map.entrySet();
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        try (var structWriter = writer.serializeStruct(size())) {
            for (final var entry : map.entrySet()) {
                structWriter.serializeField(entry.getKey(), entry.getValue(), JsonValueSerializer.INSTANCE);
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final JsonObject other)) {
            return false;
        }
        return map.equals(other.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return JsonObject.class.getSimpleName() + map;
    }
}
