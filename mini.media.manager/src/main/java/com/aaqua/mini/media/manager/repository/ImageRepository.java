package com.aaqua.mini.media.manager.repository;

import com.aaqua.mini.media.manager.entity.Image;
import com.aaqua.mini.media.manager.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Optional;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public void save(final Image image) {
        log.info("ImageRepository class -> save, {}", image);

        try {
            DynamoDbTable<Image> imageDynamoDbTable = getDynamoDbTable();

            imageDynamoDbTable.putItem(image);

        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public Optional<Image> findImageById(final String id) {
        log.info("ImageRepository class -> findImageById, id: {}", id);

        try {
            DynamoDbTable<Image> imageDynamoDbTable = getDynamoDbTable();

            Key key = Key.builder()
                    .partitionValue(id)
                    .build();

            return Optional.ofNullable(imageDynamoDbTable.getItem(key));
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public void deleteImageById(final String id) {
        log.info("ImageRepository class -> deleteImageById, id: {}", id);

        try {
            DynamoDbTable<Image> imageDynamoDbTable = getDynamoDbTable();

            Key key = Key.builder()
                    .partitionValue(id)
                    .build();

            imageDynamoDbTable.deleteItem(key);
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public void deleteAll() {
        log.info("ImageRepository class -> deleteAll");

        try {
            DynamoDbTable<Image> imageDynamoDbTable = getDynamoDbTable();

            for (Image image : imageDynamoDbTable.scan().items()) {
                Key key = Key.builder()
                        .partitionValue(image.getId())
                        .build();

                imageDynamoDbTable.deleteItem(key);
            }
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    private DynamoDbTable<Image> getDynamoDbTable() {
        return dynamoDbEnhancedClient.table("Image", TableSchema.fromBean(Image.class));
    }
}
