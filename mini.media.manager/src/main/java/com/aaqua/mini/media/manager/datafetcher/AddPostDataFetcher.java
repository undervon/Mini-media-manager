package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.Image;
import com.aaqua.mini.media.manager.entity.Post;
import com.aaqua.mini.media.manager.exception.GenericException;
import com.aaqua.mini.media.manager.exception.ImageNotFoundException;
import com.aaqua.mini.media.manager.model.AddPostInput;
import com.aaqua.mini.media.manager.model.enums.Status;
import com.aaqua.mini.media.manager.repository.ImageRepository;
import com.aaqua.mini.media.manager.repository.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddPostDataFetcher {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public Post addPost(@InputArgument String id, @InputArgument AddPostInput addPostInput) {
        log.info("addPost, id: {}, {}", id, addPostInput);

        try {
            List<Image> images = new ArrayList<>();

            for (String attachment : addPostInput.getAttachments()) {
                Image image = imageRepository.findImageById(attachment)
                        .orElseThrow(() -> new ImageNotFoundException(attachment));

                String key = image.getId();

                PresignedGetObjectRequest presignedGetObjectRequest = presignedGetObject(key);

                URLConnection connection = presignedGetObjectRequest.url().openConnection();
                InputStream content = connection.getInputStream();

                s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucketName)                // the bucket name
                                .key(key)                          // the name of the object inserted into the bucket (key)
                                .contentType(MediaType.IMAGE_PNG_VALUE)
                                .build(),
                        RequestBody.fromInputStream(content, content.available()));

                image.setPath(s3Client.utilities().getUrl(GetUrlRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build())
                        .toString());
                image.setStatus(Status.ONLINE);

                imageRepository.save(image);

                images.add(image);
            }

            Post newPost = Post.builder()
                    .id(id)
                    .title(addPostInput.getTitle())
                    .attachments(images)
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

    private PresignedGetObjectRequest presignedGetObject(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest);
    }
}
