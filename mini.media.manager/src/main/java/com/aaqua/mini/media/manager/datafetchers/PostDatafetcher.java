package com.aaqua.mini.media.manager.datafetchers;

import com.aaqua.mini.media.manager.entities.Post;
import com.aaqua.mini.media.manager.models.Image;
import com.aaqua.mini.media.manager.repositories.PostRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class PostDatafetcher {

    private final AmazonS3 amazonS3;

    private final PostRepository postRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public Post addPost(@InputArgument String description, @InputArgument String imageInput) {
        log.info("addPost, description: {}, imageInput: {}", description, imageInput);

        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
            amazonS3.listBuckets().forEach(log::info);
        }

        try {
            File file = ResourceUtils.getFile("classpath:images");
            Path path = Paths.get(file.getAbsolutePath()).resolve(imageInput);

            amazonS3.putObject(
                    bucketName,
                    imageInput,
                    path.toFile()
            );
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }

        Post newPost = Post.builder()
                .image(Image.builder()
                        .url(amazonS3.getObject(bucketName, imageInput)
                                .getObjectContent()
                                .getHttpRequest()
                                .getURI()
                                .toString())
                        .build())
                .description(description)
                .build();

        return postRepository.save(newPost);
    }

    @DgsMutation
    public String deletePosts() {
        log.info("deletePosts");

        postRepository.deleteAll();

        return "Deleting all posts successfully!";
    }

    @DgsQuery
    public List<Post> getPosts() {
        log.info("getPosts");

        return Lists.newArrayList(postRepository.findAll());
    }
}
