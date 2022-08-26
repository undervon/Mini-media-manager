package com.aaqua.mini.media.manager.exception.handling;

import com.aaqua.mini.media.manager.exception.BadCredentialsException;
import com.aaqua.mini.media.manager.exception.GenericException;
import com.aaqua.mini.media.manager.exception.ImageNotFoundException;
import com.aaqua.mini.media.manager.exception.PostNotFoundException;
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
        Throwable throwable = dataFetcherExceptionHandlerParameters.getException();

        if (throwable instanceof BadCredentialsException) {

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Invalid username or password!")
                    .errorType(ErrorType.UNAUTHENTICATED)
                    .path(dataFetcherExceptionHandlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            log.error("thrown BadCredentialsException");

            return CompletableFuture.completedFuture(result);
        } else if (throwable instanceof GenericException) {

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Something wrong was done!")
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(dataFetcherExceptionHandlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            log.error("thrown GenericException");

            return CompletableFuture.completedFuture(result);
        } else if (throwable instanceof PostNotFoundException) {

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("The post '%s' not found in DB.", throwable.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .path(dataFetcherExceptionHandlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            log.error("thrown PostNotFoundException");

            return CompletableFuture.completedFuture(result);
        } else if (throwable instanceof ImageNotFoundException) {

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("The image '%s' not found in DB.", throwable.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .path(dataFetcherExceptionHandlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            log.error("thrown ImageNotFoundException");

            return CompletableFuture.completedFuture(result);
        } else {
            return DataFetcherExceptionHandler.super.handleException(dataFetcherExceptionHandlerParameters);
        }
    }
}
