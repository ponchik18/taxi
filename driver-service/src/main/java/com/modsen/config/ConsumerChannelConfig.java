package com.modsen.config;

import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.handler.RideDriverRequestHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.PollableChannel;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ConsumerChannelConfig {
    @Value("${spring.integration.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.integration.kafka.accepted-topic}")
    private String springIntegrationKafkaAcceptedTopic;

    @Value("${spring.integration.kafka.group_id}")
    private String springIntegrationKafkaGroupId;


    @Bean
    public PollableChannel consumerChannel() {
        return new QueueChannel();
    }

    @Bean
    public KafkaMessageDrivenChannelAdapter<String, RideDriverRequest> kafkaMessageDrivenChannelAdapter() {
        KafkaMessageDrivenChannelAdapter<String, RideDriverRequest> kafkaMessageDrivenChannelAdapter = new KafkaMessageDrivenChannelAdapter<>(kafkaListenerContainer());
        kafkaMessageDrivenChannelAdapter.setOutputChannel(consumerChannel());

        return kafkaMessageDrivenChannelAdapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "consumerChannel")
    public RideDriverRequestHandler rideDriverRequestHandler() {
        return new RideDriverRequestHandler();
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, RideDriverRequest> kafkaListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties(springIntegrationKafkaAcceptedTopic);

        return new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
    }

    @Bean
    public ConsumerFactory<String, RideDriverRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, springIntegrationKafkaGroupId);
        // automatically reset the offset to the earliest offset
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put("spring.json.trusted.packages", "com.modsen.dto.rating, com.modsen.dto.card, com.modsen.dto.promo,com.modsen.dto.rides,com.modsen.dto.payment");
        return properties;
    }
}