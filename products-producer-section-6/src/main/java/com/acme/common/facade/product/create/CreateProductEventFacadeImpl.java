package com.acme.common.facade.product.create;

import com.acme.common.constants.KafkaConstants;
import com.acme.common.domain.Product;
import com.acme.common.events.ProductCreatedEvent;
import com.acme.common.facade.product.create.mapper.CreateProductDomainToEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProductEventFacadeImpl implements CreateProductEventFacade {

    private final CreateProductDomainToEventMapper createProductDomainToEventMapper;

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Override
    public void publishEvent(Product product) throws Exception {
        log.info("Publishing event to product created [{}] into kafka.", product);

        ProductCreatedEvent productCreatedEvent =
                createProductDomainToEventMapper.apply(product);

        log.info("It will be published the event: [{}].", productCreatedEvent);

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(KafkaConstants.PRODUCTS_CREATED_TOPIC_NAME,
                        productCreatedEvent.getId(), productCreatedEvent).get();

        log.info("Partition [{}], Topic [{}], Offset [{}]",
                result.getRecordMetadata().partition(), result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset());

        log.info("Event product created was published.");

    }
}
