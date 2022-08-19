package com.aaqua.mini.media.manager.repository;

import com.aaqua.mini.media.manager.entity.Post;
import com.aaqua.mini.media.manager.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public Post save(final Post post) {
        log.info("PostRepository class -> save, {}", post);

        try {
            DynamoDbTable<Post> postDynamoDbTable = getDynamoDbTable();

            postDynamoDbTable.putItem(post);

            return post;
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public void deleteAll() {
        log.info("PostRepository class -> deleteAll");

        try {
            DynamoDbTable<Post> postDynamoDbTable = getDynamoDbTable();

            for (Post post : postDynamoDbTable.scan().items()) {
                Key key = Key.builder()
                        .partitionValue(post.getId())
                        .build();

                postDynamoDbTable.deleteItem(key);
            }
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public void deleteById(final String id) {
        log.info("PostRepository class -> deleteById, id: {}", id);

        try {
            DynamoDbTable<Post> postDynamoDbTable = getDynamoDbTable();

            Key key = Key.builder()
                    .partitionValue(id)
                    .build();

            postDynamoDbTable.deleteItem(key);
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public List<Post> findAll() {
        log.info("PostRepository class -> findAll");

        try {
            DynamoDbTable<Post> postDynamoDbTable = getDynamoDbTable();

            return postDynamoDbTable.scan()
                    .items()
                    .stream()
                    .collect(Collectors.toList());
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public Optional<Post> findPostById(final String id) {
        log.info("PostRepository class -> findPostById, id: {}", id);

        try {
            DynamoDbTable<Post> postDynamoDbTable = getDynamoDbTable();

            Key key = Key.builder()
                    .partitionValue(id)
                    .build();

            return Optional.ofNullable(postDynamoDbTable.getItem(key));
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    private DynamoDbTable<Post> getDynamoDbTable() {
        return dynamoDbEnhancedClient.table("Post", TableSchema.fromBean(Post.class));
    }
}
