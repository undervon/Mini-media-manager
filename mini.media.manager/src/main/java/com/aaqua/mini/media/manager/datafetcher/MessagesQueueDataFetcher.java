package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.exception.GenericException;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class MessagesQueueDataFetcher {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @DgsQuery
    public String receiveAndDeleteSqsMessage() {
        log.info("receiveAndDeleteSqsMessage");

        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .build();

            List<Message> receivedMessages = sqsAsyncClient.receiveMessage(receiveMessageRequest)
                    .get()
                    .messages();

            String message = "";
            for (Message receivedMessage : receivedMessages) {
                message = receivedMessage.body();

                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(receivedMessage.receiptHandle())
                        .build();
                sqsAsyncClient.deleteMessage(deleteMessageRequest);
            }
            return message;
        } catch (SqsException sqsException) {
            log.error(sqsException.awsErrorDetails().errorMessage());
            throw new GenericException();
        } catch (ExecutionException | InterruptedException exception) {
            log.info(exception.getMessage());
            throw new GenericException();
        }
    }
}
