package com.aaqua.mini.media.manager.repository;

import com.aaqua.mini.media.manager.entity.User;
import com.aaqua.mini.media.manager.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Log4j2
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public User save(final User user) {
        log.info("UserRepository class -> save, {}", user);

        try {
            DynamoDbTable<User> userDynamoDbTable = getDynamoDbTable();

            userDynamoDbTable.putItem(user);

            return user;
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    public boolean existsUserByEmailAndPassword(final String email, final String password) {
        log.info("UserRepository class -> existsUserByEmailAndPassword, email: {}, password: {}", email, password);

        try {
            DynamoDbTable<User> userDynamoDbTable = getDynamoDbTable();

            for (User user : userDynamoDbTable.scan().items()) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    return true;
                }
            }

            return false;
        } catch (DynamoDbException dynamoDbException) {
            log.error(dynamoDbException.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    private DynamoDbTable<User> getDynamoDbTable() {
        return dynamoDbEnhancedClient.table("User", TableSchema.fromBean(User.class));
    }
}
