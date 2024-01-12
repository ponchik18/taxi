package com.modsen.integration;

import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideResponseWithDriver;
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
@ContextConfiguration(classes = RidesServiceIntegrationTestConfiguration.class)
public class RideDriverRequestHandlerIntegrationTest extends TestBaseContainer {

    @Autowired
    private KafkaTemplate<String, Object> testKafkaTemplate;
    @Autowired
    private KafkaConsumer<Object, Object> testKafkaConsumer;
    @Value("${spring.integration.kafka.accepted-topic}")
    private String acceptedTopic;


    @Test
    public void handleMessage_ValidRideDriverRequest_Success() {
        RideResponseWithDriver rideDriverRequest = RideResponseWithDriver.builder()
                .driverId(RidesServiceTestConstants.TestData.DRIVER_ID_1)
                .rideDriverRequest(new RideDriverRequest())
                .build();
        testKafkaConsumer.subscribe(Collections.singletonList(acceptedTopic));

        testKafkaTemplate.send(acceptedTopic, rideDriverRequest);

        ConsumerRecords<Object, Object> records = testKafkaConsumer.poll(Duration.ofSeconds(5));
        assertThat(records.count())
                .isEqualTo(1);
    }
}