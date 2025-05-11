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

package jserde.service.ser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import jserde.core.ser.service.DataValueWriterService;
import org.jspecify.annotations.Nullable;

/**
 * Serialization service locator based on {@link ServiceLoader}.
 *
 * @author Laurent Pireyn
 */
@SuppressWarnings("checkstyle:ConstantName")
public final class SerializationServiceLocator {
    private static final Map<String, List<DataValueWriterService>> dataValueWriterServicesByFormat = new HashMap<>();
    private static final Map<String, List<DataValueWriterService>> dataValueWriterServicesByMediaType = new HashMap<>();
    private static final Map<String, List<DataValueWriterService>> dataValueWriterServicesByFileExtension = new HashMap<>();
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
        final var loader = ServiceLoader.load(DataValueWriterService.class);
        for (final var service : loader) {
            final var format = service.getFormat();
            dataValueWriterServicesByFormat.computeIfAbsent(
                format,
                key -> new ArrayList<>(2)
            )
                .add(service);
            for (final var mediaType : service.getSupportedMediaTypes()) {
                dataValueWriterServicesByMediaType.computeIfAbsent(
                        mediaType,
                        key -> new ArrayList<>(2)
                    )
                    .add(service);
            }
            for (final var fileExtension : service.getSupportedFileExtensions()) {
                dataValueWriterServicesByFileExtension.computeIfAbsent(
                        fileExtension,
                        key -> new ArrayList<>(2)
                    )
                    .add(service);
            }
        }
        servicesRegistered = true;
    }

    /**
     * Resolves and returns the {@link DataValueWriterService} for the given format.
     *
     * @param format the canonical name of the format
     * @return the data value writer service for {@code format},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code format}
     */
    public static @Nullable DataValueWriterService findDataValueWriterServiceByFormat(String format) {
        registerServices();
        return singleServiceOrNull(dataValueWriterServicesByFormat.get(format));
    }

    /**
     * Resolves and returns the {@link DataValueWriterService} for the given media type.
     *
     * @param mediaType the media type
     * @return the data value writer service for {@code mediaType},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code mediaType}
     */
    public static @Nullable DataValueWriterService findDataValueWriterServiceByMediaType(String mediaType) {
        registerServices();
        return singleServiceOrNull(dataValueWriterServicesByMediaType.get(mediaType));
    }

    /**
     * Resolves and returns the {@link DataValueWriterService} for the given file extension.
     *
     * @param fileExtension the file extension (without leading dot)
     * @return the data value writer service for {@code fileExtension},
     * or {@code null} if no services could be resolved
     * @throws IllegalStateException if more than one service is resolved for {@code fileExtension}
     */
    public static @Nullable DataValueWriterService findDataValueWriterServiceByFileExtension(String fileExtension) {
        registerServices();
        return singleServiceOrNull(dataValueWriterServicesByFileExtension.get(normalizedFileExtension(fileExtension)));
    }

    private SerializationServiceLocator() {}
}
