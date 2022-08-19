package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.Post;
import com.aaqua.mini.media.manager.exception.GenericException;
import com.aaqua.mini.media.manager.model.AddPostInput;
import com.aaqua.mini.media.manager.model.Image;
import com.aaqua.mini.media.manager.repository.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddPostDataFetcher {

    private final S3Client s3Client;

    private final PostRepository postRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public Post addPost(@InputArgument String id, @InputArgument AddPostInput addPostInput) {
        log.info("addPost, id: {}, {}", id, addPostInput);

        try {
            File file = ResourceUtils.getFile("classpath:images");
            List<Path> paths = addPostInput.getImages().stream()
                    .map(imageName -> Paths.get(file.getAbsolutePath()).resolve(imageName))
                    .collect(Collectors.toList());

            List<Image> images = new ArrayList<>();

            for (Path path : paths) {
                UUID key = UUID.randomUUID();

                s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucketName)                 // the bucket name
                                .key(key.toString())                // the name of the object inserted into the bucket (key)
                                .build(),
                        path);

                images.add(Image.builder()
                        .key(key.toString())
                        .url(s3Client.utilities().getUrl(GetUrlRequest.builder()
                                        .bucket(bucketName)
                                        .key(key.toString())
                                        .build())
                                .toString())
                        .build());
            }

            Post newPost = Post.builder()
                    .id(id)
                    .title(addPostInput.getTitle())
                    .images(images)
                    .description(addPostInput.getDescription()).build();

            return postRepository.save(newPost);
        } catch (IOException | UncheckedIOException exception) {
            log.error(exception.getMessage());
            throw new GenericException();
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }
}
