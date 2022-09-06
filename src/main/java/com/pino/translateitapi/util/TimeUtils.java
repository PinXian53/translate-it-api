package com.pino.translateitapi.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class TimeUtils {

    private TimeUtils(){}

    public static OffsetDateTime toOffsetDateTime(Long millis) {
        return Optional.ofNullable(millis)
            .map(utcMillis -> Instant.ofEpochMilli(utcMillis).atOffset(ZoneOffset.UTC))
            .orElse(null);
    }

    public static Long toUTCMilliseconds(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
            .map(val -> val.toInstant().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli())
            .orElse(null);
    }

    public static OffsetDateTime generateByYYYYMMDD(Integer year, Integer month, Integer day) {
        return OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
    }
}
