package com.modsen.handler;

import com.modsen.dto.RideRequest;
import com.modsen.dto.RideResponseWithDriver;
import com.modsen.enums.DriverStatus;
import com.modsen.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class RideRequestHandler implements MessageHandler {

    private DriverRepository driverRepository;
    private MessageChannel producingChannel;
    @Value("${spring.kafka.sent-topic}")
    private String springIntegrationKafkaSentTopic;
    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Autowired
    public void setProducingChannel(MessageChannel producingChannel) {
        this.producingChannel = producingChannel;
    }

    @Override
    @Transactional
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        RideRequest rideRequest = (RideRequest) message.getPayload();

        driverRepository.findByDriverStatus(DriverStatus.AVAILABLE).ifPresent(driver -> {
            RideResponseWithDriver rideResponseWithDriver = RideResponseWithDriver.builder()
                    .rideRequest(rideRequest)
                    .driverId(driver.getId())
                    .build();

            driver.setDriverStatus(DriverStatus.BUSY);
            driverRepository.save(driver);

            producingChannel.send(new GenericMessage<>(
                    rideResponseWithDriver,
                    Collections.singletonMap(KafkaHeaders.TOPIC, springIntegrationKafkaSentTopic))
            );
        });
    }

}