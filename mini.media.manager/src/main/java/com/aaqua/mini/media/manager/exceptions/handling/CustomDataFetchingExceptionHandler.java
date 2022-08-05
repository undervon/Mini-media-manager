package com.aaqua.mini.media.manager.exceptions.handling;

import com.aaqua.mini.media.manager.exceptions.BadCredentialsException;
import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
public class CustomDataFetchingExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters dataFetcherExceptionHandlerParameters) {
        if (dataFetcherExceptionHandlerParameters.getException() instanceof BadCredentialsException) {

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Invalid username or password!")
                    .errorType(ErrorType.UNAUTHENTICATED)
                    .path(dataFetcherExceptionHandlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            log.error("thrown BadCredentialsException");

            return CompletableFuture.completedFuture(result);
        } else {
            return DataFetcherExceptionHandler.super.handleException(dataFetcherExceptionHandlerParameters);
        }
    }
}
