package com.modsen.handler;

import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideResponseWithDriver;
import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import com.modsen.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collections;
import java.util.List;

public class RideDriverRequestHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(RideDriverRequestHandler.class);
    private DriverRepository driverRepository;
    private MessageChannel producingChannel;
    @Value("${spring.integration.kafka.sent-topic}")
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
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        log.info("Received Message in class RideDriverRequestHandler: " + message);
        RideDriverRequest rideDriverRequest = (RideDriverRequest) message.getPayload();

        List<Driver> drivers = driverRepository.findAllByDriverStatus(DriverStatus.AVAILABLE);
        if (!drivers.isEmpty()) {
            Driver driver = drivers.get(0);
            RideResponseWithDriver rideResponseWithDriver = RideResponseWithDriver.builder()
                    .rideDriverRequest(rideDriverRequest)
                    .driverId(driver.getId())
                    .build();

            driver.setDriverStatus(DriverStatus.BUSY);
            driverRepository.save(driver);
            producingChannel.send(new GenericMessage<>(
                    rideResponseWithDriver,
                    Collections.singletonMap(KafkaHeaders.TOPIC, springIntegrationKafkaSentTopic))
            );
            log.info("Message in class RideDriverRequestHandler was be sent back");
        }
    }
}