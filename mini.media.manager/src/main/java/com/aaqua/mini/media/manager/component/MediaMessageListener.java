package com.aaqua.mini.media.manager.component;

import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MediaMessageListener {

    @QueueListener("${aws.sqs.queueUrl}")
    public String listen(@Payload String payload) {
        log.info(payload);

        return payload;
    }
}
