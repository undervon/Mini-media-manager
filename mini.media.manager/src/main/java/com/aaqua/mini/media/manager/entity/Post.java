package com.aaqua.mini.media.manager.entity;

import com.aaqua.mini.media.manager.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private String id;
    private String title;
    private List<Image> images;
    private String description;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
