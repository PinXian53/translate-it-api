package com.pino.translateitapi.exception.handler;

import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.exception.InternalServerErrorException;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    public GraphQLError resolveToSingleError(Throwable e, DataFetchingEnvironment env) {
        if (e instanceof BadRequestException) {
            return toGraphQLError(e.getMessage(), e, env);
        } else if (e instanceof InternalServerErrorException) {
            return toGraphQLError("系統發生錯誤，請稍後再試", e, env);
        } else if (e instanceof MaxUploadSizeExceededException) {
            return toGraphQLError("上傳的檔案大小超過 5 MB", e, env);
        } else if (e instanceof ConstraintViolationException constraintviolationexception) {
            // 資料有問題驗證不通過
            String errorMessage = constraintviolationexception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            return toGraphQLError(errorMessage, e, env);
        }
        return toGraphQLError("未知的錯誤，請稍後再試", e, env);
    }

    private GraphQLError toGraphQLError(String message, Throwable e, DataFetchingEnvironment env) {
        String executionId = env.getExecutionId().toString();
        String exceptionType = e.getClass().getSimpleName();
        log.error("ExecutionId：{}, Exception Type：{}, Error Message：{}\n{}", executionId, exceptionType, message,
            getStackTrace(e));
        Map<String, Object> extension = new HashMap<>();
        extension.put("ExecutionId", executionId);
        extension.put("ExceptionType", exceptionType);
        return GraphqlErrorBuilder.newError()
            .message(message)
            .path(env.getExecutionStepInfo().getPath())
            .errorType(ErrorType.DataFetchingException)
            .extensions(extension)
            .build();
    }

    private String getStackTrace(Throwable e) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(e.getStackTrace())
            .forEach(element -> stringBuilder.append("    ").append(element.toString()).append("\n"));
        return stringBuilder.toString();
    }

}
