package com.modsen.integration;

import com.modsen.constants.RidesServiceConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class RidesServiceIntegrationTestConfiguration {

    @Value("${spring.integration.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> testProducerFactory() {
        return new DefaultKafkaProducerFactory<>(testProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> testKafkaTemplate() {
        return new KafkaTemplate<>(testProducerFactory());
    }

    private Map<String, Object> testProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(
                RidesServiceConstants.KafkaProperties.TRUSTED_PACKAGE_KEY,
                RidesServiceConstants.KafkaProperties.TRUSTED_PACKAGE_VALUE
        );
        return props;
    }

    private Map<String, Object> testConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-kafka");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(
                RidesServiceConstants.KafkaProperties.TRUSTED_PACKAGE_KEY,
                RidesServiceConstants.KafkaProperties.TRUSTED_PACKAGE_VALUE
        );
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                RidesServiceConstants.KafkaProperties.AUTO_OFFSET_RESET_CONFIG_VALUE
        );
        return props;
    }

    @Bean
    public KafkaConsumer<Object, Object> testKafkaConsumer() {
        return new KafkaConsumer<>(testConsumerConfig());
    }
}