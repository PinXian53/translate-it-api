package com.pino.translateitapi.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

public class ModelMapperUtils {

    private ModelMapperUtils() {}

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
            // 預設的模式可能導致資料匹配錯誤(ex. bookOid 可以匹配到 oid)，所以設為嚴格模式
            .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <T> T map(Object source, Class<T> targetClazz) {
        return modelMapper.map(source, targetClazz);
    }

    public static <T> List<T> mapList(List<?> source, Class<T> targetClazz) {
        return source.stream().map(o -> ModelMapperUtils.map(o, targetClazz)).toList();
    }
}
