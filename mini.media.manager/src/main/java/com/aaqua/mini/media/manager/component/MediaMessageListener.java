package com.aaqua.mini.media.manager.component;

import com.aaqua.mini.media.manager.entity.Image;
import com.aaqua.mini.media.manager.exception.ImageNotFoundException;
import com.aaqua.mini.media.manager.model.enums.Status;
import com.aaqua.mini.media.manager.repository.ImageRepository;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class MediaMessageListener {

    private final ImageRepository imageRepository;

    @QueueListener("${aws.sqs.queueUrl}")
    public void listen(@Payload final String payload) {
        log.info(payload);

        JSONObject payloadJson = new JSONObject(payload);
        JSONArray payloadJsonArray = payloadJson.getJSONArray("Records");
        payloadJson = payloadJsonArray.getJSONObject(0);
        payloadJson = payloadJson.getJSONObject("s3");
        payloadJson = payloadJson.getJSONObject("object");

        String key = payloadJson.getString("key");

        Image image = imageRepository.findImageById(key)
                .orElseThrow(() -> new ImageNotFoundException(key));

        image.setStatus(Status.ONLINE);

        imageRepository.save(image);
    }
}
