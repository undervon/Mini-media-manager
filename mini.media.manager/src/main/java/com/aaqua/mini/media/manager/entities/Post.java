package com.aaqua.mini.media.manager.entities;

import com.aaqua.mini.media.manager.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private String id;
    private String title;
    private Image image;
    private String description;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
