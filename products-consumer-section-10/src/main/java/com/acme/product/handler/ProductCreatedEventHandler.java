package com.acme.product.handler;

import com.acme.common.constants.KafkaConstants;
import com.acme.common.events.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreatedEventHandler {

    /**
     * KafkaListener annotation goes on the method, not the class, unless you want to use a multi-method listener,
     * in which case you need @KafkaHandler on the methods.
     *
     * @param productCreatedEvent an instance of ProductCreatedEvent which represents the event/message created
     *                            by the producer.
     */
    //@KafkaHandler
    @KafkaListener(topics = KafkaConstants.PRODUCTS_CREATED_TOPIC_NAME)
    public void handler(ProductCreatedEvent productCreatedEvent) {

        log.info("Received event: [{}].", productCreatedEvent);

    }
}
