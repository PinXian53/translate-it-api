package com.pino.translateitapi.util;

import java.util.Optional;

public class ObjectUtils {

    private ObjectUtils() {}

    public static <T> T get(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }
}
