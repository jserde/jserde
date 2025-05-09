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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import jserde.core.de.service.DataValueReaderService;
import org.jspecify.annotations.Nullable;

/**
 * Deserialization service locator based on {@link ServiceLoader}.
 *
 * @author Laurent Pireyn
 */
@SuppressWarnings("checkstyle:ConstantName")
public final class DeserializationServiceLocator {
    private static final Map<String, List<DataValueReaderService>> dataValueReaderServicesByFormat = new HashMap<>();
    private static final Map<String, List<DataValueReaderService>> dataValueReaderServicesByMediaType = new HashMap<>();
    private static final Map<String, List<DataValueReaderService>> dataValueReaderServicesByFileExtension = new HashMap<>();
    private static boolean servicesRegistered;

    private static <S> @Nullable S singleServiceOrNull(@Nullable List<S> services) {
        if (services == null || services.isEmpty()) {
            return null;
        }
        final var count = services.size();
        if (count > 1) {
            throw new IllegalStateException(count + " provider services found");
        }
        return services.get(0);
    }

    private static String normalizedFileExtension(String fileExtension) {
        return !fileExtension.isEmpty() && fileExtension.charAt(0) == '.'
            ? fileExtension.substring(1)
            : fileExtension;
    }

    private static synchronized void registerServices() {
        if (servicesRegistered) {
            return;
        }
        final var loader = ServiceLoader.load(DataValueReaderService.class);
        for (final var service : loader) {
            final var format = service.getFormat();
            dataValueReaderServicesByFormat.computeIfAbsent(
                format,
                key -> new ArrayList<>(2)
            )
                .add(service);
            for (final var mediaType : service.getSupportedMediaTypes()) {
                dataValueReaderServicesByMediaType.computeIfAbsent(
                        mediaType,
                        key -> new ArrayList<>(2)
                    )
                    .add(service);
            }
            for (final var fileExtension : service.getSupportedFileExtensions()) {
                dataValueReaderServicesByFileExtension.computeIfAbsent(
                        fileExtension,
                        key -> new ArrayList<>(2)
                    )
                    .add(service);
            }
        }
        servicesRegistered = true;
    }

    /**
     * Resolves and returns the {@link DataValueReaderService} for the given format.
     *
     * @param format the canonical name of the format
     * @return the data value reader service for {@code format},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code format}
     */
    public static @Nullable DataValueReaderService findDataValueReaderServiceByFormat(String format) {
        registerServices();
        return singleServiceOrNull(dataValueReaderServicesByFormat.get(format));
    }

    /**
     * Resolves and returns the {@link DataValueReaderService} for the given media type.
     *
     * @param mediaType the media type
     * @return the data value reader service for {@code mediaType},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code mediaType}
     */
    public static @Nullable DataValueReaderService findDataValueReaderServiceByMediaType(String mediaType) {
        registerServices();
        return singleServiceOrNull(dataValueReaderServicesByMediaType.get(mediaType));
    }

    /**
     * Resolves and returns the {@link DataValueReaderService} for the given file extension.
     *
     * @param fileExtension the file extension (without leading dot)
     * @return the data value reader service for {@code fileExtension},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code fileExtension}
     */
    public static @Nullable DataValueReaderService findDataValueReaderServiceByFileExtension(String fileExtension) {
        registerServices();
        return singleServiceOrNull(dataValueReaderServicesByFileExtension.get(normalizedFileExtension(fileExtension)));
    }

    private DeserializationServiceLocator() {}
}
