package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.Image;
import com.aaqua.mini.media.manager.exception.GenericException;
import com.aaqua.mini.media.manager.model.ResponsePresignedUrl;
import com.aaqua.mini.media.manager.model.enums.Status;
import com.aaqua.mini.media.manager.repository.ImageRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class GeneratePresignedUrlDataFetcher {

    private final S3Presigner s3Presigner;

    private final ImageRepository imageRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @DgsMutation
    public ResponsePresignedUrl generatePresignedUrl() {
        log.info("generatePresignedUrl");

        try {
            UUID key = UUID.randomUUID();

            PresignedPutObjectRequest presignedPutObjectRequest = presignedPutObject(key.toString());

            URL url = presignedPutObjectRequest.url();

            Image image = Image.builder()
                    .id(key.toString())
                    .path("")
                    .status(Status.PENDING)
                    .build();

            imageRepository.save(image);

            return ResponsePresignedUrl.builder()
                    .image(image)
                    .url(url.toString())
                    .build();
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }

    private PresignedPutObjectRequest presignedPutObject(String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(putObjectPresignRequest);
    }
}
