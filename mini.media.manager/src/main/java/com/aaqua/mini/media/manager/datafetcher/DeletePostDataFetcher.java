package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.Post;
import com.aaqua.mini.media.manager.exception.GenericException;
import com.aaqua.mini.media.manager.exception.PostNotFoundException;
import com.aaqua.mini.media.manager.repository.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class DeletePostDataFetcher {

    private final S3Client s3Client;

    private final PostRepository postRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public String deletePosts() {
        log.info("deletePosts");

        postRepository.deleteAll();

        try {
            List<ObjectIdentifier> keys = new ArrayList<>();

            List<S3Object> s3Objects = s3Client.listObjects(ListObjectsRequest.builder()
                            .bucket(bucketName)
                            .build())
                    .contents();

            for (S3Object s3Object : s3Objects) {
                keys.add(ObjectIdentifier.builder()
                        .key(s3Object.key())
                        .build());
            }

            // check if the bucket contains objects
            if (!keys.isEmpty()) {
                Delete delete = Delete.builder()
                        .objects(keys)
                        .build();

                s3Client.deleteObjects(DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(delete)
                        .build());
            }
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }

        return "Deleting all posts successfully!";
    }

    @DgsMutation
    public String deletePostById(@InputArgument String id) {
        log.info("deletePostById, id: {}", id);

        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(post.getImage().getKey())
                    .build());
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }

        postRepository.deleteById(id);

        return String.format("Deleting post '%s' successfully!", id);
    }
}
