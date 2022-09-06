package com.pino.translateitapi.config;

import com.pino.translateitapi.util.TimeUtils;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.OffsetDateTime;

@Configuration
public class GraphQLScalarTypeConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(Timestamp).scalar(Oid);
    }

    public static final GraphQLScalarType Timestamp = GraphQLScalarType.newScalar()
        .name("Timestamp")
        .description("時間戳(毫秒)")
        .coercing(new Coercing<OffsetDateTime, Long>() {
            // 輸出到前端
            @Override
            public Long serialize(Object val) {
                return TimeUtils.toUTCMilliseconds((OffsetDateTime) val);
            }

            // 從前端 variables 進來的 input
            @Override
            public OffsetDateTime parseValue(Object input) {
                return TimeUtils.toOffsetDateTime((Long) input);
            }

            // 從前端 query 字串進來的 input
            @Override
            public OffsetDateTime parseLiteral(Object input) {
                return TimeUtils.toOffsetDateTime((Long) input);
            }
        })
        .build();

    public static final GraphQLScalarType Oid = GraphQLScalarType.newScalar()
        .name("Oid")
        .description("識別碼(數字型態 ID)")
        .coercing(new Coercing<Integer, Integer>() {
            @Override
            public Integer serialize(Object val) {
                return (Integer) val;
            }
            @Override
            public Integer parseValue(Object input) {
                return (Integer) input;
            }
            @Override
            public Integer parseLiteral(Object input) {
                return (Integer) input;
            }
        })
        .build();
}

