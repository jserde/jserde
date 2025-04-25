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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import jserde.core.ser.DataValueWriter;
import jserde.json.JsonValueType;
import org.jspecify.annotations.Nullable;

/**
 * JSON array.
 *
 * <p>Implementation: this class is backed by an {@link ArrayList}.
 *
 * @author Laurent Pireyn
 */
public final class JsonArray extends JsonValue implements List<JsonValue> {
    public static final int DEFAULT_INITIAL_CAPACITY = 10;

    private final List<JsonValue> list;

    private JsonArray(List<JsonValue> list) {
        this.list = list;
    }

    public JsonArray() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public JsonArray(int initialCapacity) {
        this(new ArrayList<>(initialCapacity));
    }

    public JsonArray(Collection<? extends JsonValue> collection) {
        this(new ArrayList<>(collection));
    }

    /**
     * Returns {@link JsonValueType#ARRAY}.
     */
    @Override
    public JsonValueType getType() {
        return JsonValueType.ARRAY;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return list.iterator();
    }

    @Override
    public ListIterator<JsonValue> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<JsonValue> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public boolean contains(@Nullable Object value) {
        return list.contains(value);
    }

    @Override
    public boolean containsAll(Collection<? extends @Nullable Object> collection) {
        return list.containsAll(collection);
    }

    @Override
    public int indexOf(@Nullable Object value) {
        return list.indexOf(value);
    }

    @Override
    public int lastIndexOf(@Nullable Object value) {
        return list.lastIndexOf(value);
    }

    @Override
    public JsonValue get(int index) {
        return list.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue element) {
        return list.set(index, element);
    }

    @Override
    public boolean add(JsonValue element) {
        return list.add(element);
    }

    @Override
    public void add(int index, JsonValue element) {
        list.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends JsonValue> collection) {
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends JsonValue> collection) {
        return list.addAll(index, collection);
    }

    @Override
    public boolean remove(@Nullable Object value) {
        return list.remove(value);
    }

    @Override
    public JsonValue remove(int index) {
        return list.remove(index);
    }

    @Override
    public boolean removeAll(Collection<? extends @Nullable Object> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super JsonValue> filter) {
        return list.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<? extends @Nullable Object> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void replaceAll(UnaryOperator<JsonValue> operator) {
        list.replaceAll(operator);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void sort(Comparator<? super JsonValue> comparator) {
        list.sort(comparator);
    }

    @Override
    public void forEach(Consumer<? super JsonValue> action) {
        list.forEach(action);
    }

    @Override
    public JsonArray subList(int fromIndex, int toIndex) {
        return new JsonArray(list.subList(fromIndex, toIndex));
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return list.toArray(generator);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public Spliterator<JsonValue> spliterator() {
        return list.spliterator();
    }

    @Override
    public Stream<JsonValue> stream() {
        return list.stream();
    }

    @Override
    public Stream<JsonValue> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public void serialize(DataValueWriter writer) throws IOException {
        try (var sequenceWriter = writer.serializeSequence(size())) {
            for (final var element : list) {
                sequenceWriter.serializeElement(element, JsonValueSerializer.INSTANCE);
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final JsonArray other)) {
            return false;
        }
        return list.equals(other.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        return JsonArray.class.getSimpleName() + list;
    }
}
