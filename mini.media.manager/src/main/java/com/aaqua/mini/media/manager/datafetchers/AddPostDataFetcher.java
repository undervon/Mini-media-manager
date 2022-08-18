package com.aaqua.mini.media.manager.datafetchers;

import com.aaqua.mini.media.manager.entities.Post;
import com.aaqua.mini.media.manager.exceptions.GenericException;
import com.aaqua.mini.media.manager.models.AddPostInput;
import com.aaqua.mini.media.manager.models.Image;
import com.aaqua.mini.media.manager.repositories.PostRepository;
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
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddPostDataFetcher {

    private S3Client s3Client;

    private PostRepository postRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public Post addPost(@InputArgument String id, @InputArgument AddPostInput addPostInput) {
        log.info("addPost, id: {}, {}", id, addPostInput);

        try {
            File file = ResourceUtils.getFile("classpath:images");
            Path path = Paths.get(file.getAbsolutePath()).resolve(addPostInput.getImage());

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(path.toFile().toString())
                            .build(),
                    path);

            Post newPost = Post.builder()
                    .id(id)
                    .title(addPostInput.getTitle())
                    .image(Image.builder()
                            .url(s3Client.utilities().getUrl(GetUrlRequest.builder()
                                            .bucket(bucketName)
                                            .key(path.toFile().toString())
                                            .build())
                                    .toString())
                            .build())
                    .description(addPostInput.getDescription())
                    .build();

            return postRepository.save(newPost);
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
            throw new GenericException();
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }
}
