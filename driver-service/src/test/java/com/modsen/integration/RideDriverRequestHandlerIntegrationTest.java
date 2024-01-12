package com.modsen.integration;

import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import com.modsen.repository.DriverRepository;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = DriverServiceIntegrationTestConfiguration.class)
public class RideDriverRequestHandlerIntegrationTest extends TestBaseContainer {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private KafkaTemplate<String, Object> testProducerFactory;
    @Autowired
    private KafkaConsumer<Object, Object> testKafkaConsumer;
    @Value("${spring.integration.kafka.accepted-topic}")
    private String acceptedTopic;
    @Value("${spring.integration.kafka.sent-topic}")
    private String sentTopic;

    @Test
    public void handleMessage_ValidRideDriverRequest_Success() {
        Driver existingDriver = Driver.builder()
                .driverStatus(DriverStatus.AVAILABLE)
                .build();
        Long driverId = driverRepository.save(existingDriver).getId();
        RideDriverRequest rideDriverRequest = new RideDriverRequest();
        testKafkaConsumer.subscribe(Collections.singletonList(sentTopic));

        testProducerFactory.send(acceptedTopic, rideDriverRequest);

        ConsumerRecords<Object, Object> records = testKafkaConsumer.poll(Duration.ofSeconds(5));
        assertThat(records.count())
                .isEqualTo(1);

        Driver actualDriver = driverRepository.findById(driverId)
                .orElseThrow();
        assertThat(actualDriver.getDriverStatus())
                .isEqualTo(DriverStatus.BUSY);
    }


}