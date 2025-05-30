package com.acme.common.config.kafka;

import com.acme.common.constants.KafkaConstants;
import com.acme.common.events.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to configure the kafka producer behaviour.
 * <br/>
 * Many properties are defined in application.properties. So, if you want to change them,
 * you can do it there through next methods:
 * <ul>
 *     <li>com.acme.common.config.kafka.KafkaConfig#setProducerConfigs</li>
 *     <li>com.acme.common.config.kafka.KafkaConfig#createProducerFactory</li>
 *     <li>com.acme.common.config.kafka.KafkaConfig#createKafkaTemplate</li>
 * </ul>
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeout;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private boolean idempotence;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private Integer inflightRequestsPerConnection;

    Map<String, Object> setProducerConfigs() {

        Map<String, Object> configs = new HashMap<>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configs.put(ProducerConfig.ACKS_CONFIG, acks);
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);

        // Intentionally are missing properties to define retry behaviour in order to show
        // what configuration is more relevant.

        // By default, the value is true. However, it is a good idea to set it to true explicitly.
        // because it is easier to disable
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);

        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, inflightRequestsPerConnection);

        return configs;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> createProducerFactory() {
        return new DefaultKafkaProducerFactory<>(setProducerConfigs());
    }

    @Bean
    KafkaTemplate<String, ProductCreatedEvent> createKafkaTemplate() {
        return new KafkaTemplate<>(createProducerFactory());
    }

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name(KafkaConstants.PRODUCTS_CREATED_TOPIC_NAME)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
