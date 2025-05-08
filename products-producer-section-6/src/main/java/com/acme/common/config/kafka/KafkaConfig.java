package com.acme.common.config.kafka;

import com.acme.common.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name(KafkaConstants.PRODUCTS_CREATED_TOPIC_NAME)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
        //ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG
    }
}
