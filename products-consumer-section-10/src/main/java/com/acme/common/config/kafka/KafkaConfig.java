package com.acme.common.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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

    @Bean
    ConsumerFactory<String, Object> createConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(setConsumerConfigs());
    }

    /**
     * Use default name "kafkaListenerContainerFactory", if not Spring Boot's autoconfiguration factory
     * will use default (String) deserializer, and it will be caused next exception:
     * <br/>
     * org.springframework.messaging.converter.MessageConversionException: Cannot convert from [java.lang.String] to [] for GenericMessage
     * <br/>
     * @return an instance of ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory());

        return factory;
    }

}
