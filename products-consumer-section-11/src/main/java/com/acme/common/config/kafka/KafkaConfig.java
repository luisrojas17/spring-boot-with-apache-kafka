package com.acme.common.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to configure the kafka consumer behaviour.
 * <br/>
 * Many properties are defined in application.properties. So, if you want to change them,
 * you can do it here through next methods:
 * <ul>
 *     <li>com.acme.common.config.kafka.KafkaConfig#setConsumerConfigs</li>
 *     <li>com.acme.common.config.kafka.KafkaConfig#createConsumerFactory</li>
 *     <li>com.acme.common.config.kafka.KafkaConfig#createKafkaListenerContainerFactory</li>
 * </ul>
 */
@Configuration
public class KafkaConfig {

    @Autowired
    Environment env;

    /**
     * Set the configuration for the kafka consumer.
     *
     * @return an instance of Map<String, Object> which contains the consumer configuration.
     */
    Map<String, Object> setConsumerConfigs() {

        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                env.getProperty("spring.kafka.consumer.bootstrap-servers"));

        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // To handle errors related to deserialization
        // If any error is thrown during deserialization, it will be passed to the ErrorHandlingDeserializer
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // If no any error is thrown during deserialization, it will be passed to the JsonDeserializer
        configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        configs.put(JsonDeserializer.TRUSTED_PACKAGES,
                env.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));

        configs.put(ConsumerConfig.GROUP_ID_CONFIG,
                env.getProperty("spring.kafka.consumer.group-id"));

        return configs;
    }

    /**
     * Create an instance of ConsumerFactory according to configuration defined into
     * {@link KafkaConfig#setConsumerConfigs} method.
     *
     * @return an instance of ConsumerFactory.
     */
    @Bean
    ConsumerFactory<String, Object> createConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(setConsumerConfigs());
    }

    /**
     * Create an instance of ConcurrentKafkaListenerContainerFactory to configure the kafka consumer listener
     * inorder to consume messages from @{link KafkaConstants#PRODUCTS_CREATED_TOPIC_NAME}.
     * <br/>
     * Use default name "kafkaListenerContainerFactory", if not Spring Boot's autoconfiguration factory
     * will use default (String) deserializer, and it will be caused next exception:
     * <br/>
     * org.springframework.messaging.converter.MessageConversionException: Cannot convert from [java.lang.String] to [] for GenericMessage
     * <br/>
     * @param consumerFactory an instance of ConsumerFactory.
     * @param kafkaTemplate an instance of KafkaTemplate.
     * @return an instance of ConcurrentKafkaListenerContainerFactory.
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory, KafkaTemplate<String, Object> kafkaTemplate) {

        // To handler exceptions that occur during messages consumption by Kafka Listener
        // DeadLetterPublishingRecoverer will be used to publish messages that could not be processed.
        // Those messages will be sent to dead letter topic
        // By default dead letter topic name will be "producer-name-topic-dlq".
        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate));

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    /**
     * Create an instance of KafkaTemplate. This class wraps a producer and provides convenience methods
     * to send data to Kafka topics.
     *
     * @param producerFactory an instance of ProducerFactory.
     * @return an instanceof KafkaTemplate.
     */
    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Create an instance of ProducerFactory to send messages that could not be processed during consumer
     * consumption.
     *
     * @return an instance of ProducerFactory.
     */
    @Bean
    ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                env.getProperty("spring.kafka.consumer.bootstrap-servers"));

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }
}
