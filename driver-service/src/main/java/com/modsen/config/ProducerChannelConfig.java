package com.modsen.config;

import com.modsen.constants.DriverServiceConstants;
import com.modsen.dto.rides.RideResponseWithDriver;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerChannelConfig {
    @Value("${spring.integration.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public DirectChannel producingChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "producingChannel")
    public MessageHandler kafkaMessageHandler() {
        KafkaProducerMessageHandler<String, RideResponseWithDriver> handler =
                new KafkaProducerMessageHandler<>(kafkaTemplateRideResponseWithDriver());
        handler.setMessageKeyExpression(
                new LiteralExpression(DriverServiceConstants.KafkaProperties.MESSAGE_KEY_EXPRESSION)
        );

        return handler;
    }

    @Bean
    public KafkaTemplate<String, RideResponseWithDriver> kafkaTemplateRideResponseWithDriver() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, RideResponseWithDriver> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, DriverServiceConstants.KafkaProperties.LINGER_MS_CONFIG_VALUE);
        properties.put(
                DriverServiceConstants.KafkaProperties.TRUSTED_PACKAGE_KEY,
                DriverServiceConstants.KafkaProperties.TRUSTED_PACKAGE_VALUE
        );
        return properties;
    }

}