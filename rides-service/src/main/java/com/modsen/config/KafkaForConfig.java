package com.modsen.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaForConfig {

    @Bean
    public ProducerFactory<String, Object> customProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigsForRatingRequest());
    }

    @Bean
    public KafkaTemplate<String, Object> customKafkaTemplate() {
        return new KafkaTemplate<>(customProducerFactory());
    }

    private Map<String, Object> producerConfigsForRatingRequest() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put("spring.json.trusted.packages", "com.modsen.dto.driver");
        return props;
    }
}