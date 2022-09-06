package com.pino.translateitapi.util;

import org.modelmapper.ModelMapper;

import java.util.List;

public class ModelMapperUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T> T map(Object source, Class<T> targetClazz) {
        return modelMapper.map(source, targetClazz);
    }

    public static <T> List<T> mapList(List<?> source, Class<T> targetClazz) {
        return source.stream().map(o -> ModelMapperUtils.map(o, targetClazz)).toList();
    }
}
