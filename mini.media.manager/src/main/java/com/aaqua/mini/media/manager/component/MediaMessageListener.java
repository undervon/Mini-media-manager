package com.aaqua.mini.media.manager.component;

import com.aaqua.mini.media.manager.entity.Image;
import com.aaqua.mini.media.manager.exception.ImageNotFoundException;
import com.aaqua.mini.media.manager.model.enums.Status;
import com.aaqua.mini.media.manager.repository.ImageRepository;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class MediaMessageListener {

    private final ImageRepository imageRepository;

    @QueueListener("${aws.sqs.queueUrl}")
    public void listen(@Payload final String payload) {
        log.info("listen, payload: {}", payload);
        Image image = imageRepository.findImageById(payload)
                .orElseThrow(() -> new ImageNotFoundException(payload));

        image.setStatus(Status.ONLINE);

        imageRepository.save(image);
    }
}
