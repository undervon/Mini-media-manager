package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.exception.GenericException;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class MessagesQueueDataMutation {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @DgsMutation
    public String sendSqsMessage(@InputArgument String message) {
        log.info("sendSqsMessage, message: {}", message);

        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();
            sqsAsyncClient.sendMessage(sendMessageRequest);
        } catch (SqsException sqsException) {
            log.error(sqsException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
        return message;
    }
}
