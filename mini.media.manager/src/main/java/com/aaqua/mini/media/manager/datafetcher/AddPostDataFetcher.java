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
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddPostDataFetcher {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @DgsMutation
    public Post addPost(@InputArgument AddPostInput addPostInput) {
        log.info("addPost, {}", addPostInput);

        try {
            List<Image> images = new ArrayList<>();

            for (String attachment : addPostInput.getAttachments()) {
                Image image = imageRepository.findImageById(attachment)
                        .orElseThrow(() -> new ImageNotFoundException(attachment));

                image.setStatus(Status.ONLINE);

                imageRepository.save(image);

                images.add(image);
            }

            UUID id = UUID.randomUUID();

            Post newPost = Post.builder()
                    .id(id.toString())
                    .title(addPostInput.getTitle())
                    .attachments(images)
                    .description(addPostInput.getDescription()).build();

            return postRepository.save(newPost);
        } catch (UncheckedIOException exception) {
            log.error(exception.getMessage());
            throw new GenericException();
        } catch (S3Exception s3Exception) {
            log.error(s3Exception.awsErrorDetails().errorMessage());
            throw new GenericException();
        }
    }
}
