package com.modsen.controller;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.util.PassengerServiceClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:data/insert-test-data.sql")
public class PassengerServiceIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH;
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    public void getAllPassengers_ValidRequest_Success() {
        PassengerServiceClient.getAllPassengers();
    }

    @Test
    public void getPassengerById_ValidId_Success() {
        long passengerId = 9L;

        PassengerServiceClient.getPassenger(passengerId);
    }

    @Test
    public void createPassenger_ValidRequest_Success() {
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request45@test.com", "+375111781178");

        PassengerServiceClient.postPassenger(passengerRequest);
    }

    @Test
    public void updatePassenger_ValidIdAndRequest_Success() {
        long passengerId = 6L;
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request2@test.com", "+375111114578");
        PassengerResponse expectedPassenger = PassengerResponse.builder()
                .id(Long.valueOf(passengerId).intValue())
                .lastName(passengerRequest.getLastName())
                .firstName(passengerRequest.getFirstName())
                .phone(passengerRequest.getPhone())
                .email(passengerRequest.getEmail())
                .build();

        PassengerResponse actual = PassengerServiceClient.updatePassenger(passengerId, passengerRequest);
        assertThat(actual).isEqualTo(expectedPassenger);
    }

    @Test
    public void updatePassenger_InValidRequest_BadRequest() {
        long passengerId = 1L;

        PassengerServiceClient.updatePassengerInvalidRequest(passengerId, new PassengerRequest());
    }

    @Test
    public void createPassenger_DuplicateEmail_ExceptionThrown() {
        String email = "john.doe@example.com";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "", "+375111114578");
        passengerRequest.setEmail(email);

        PassengerServiceClient.postPassengerWithDuplicate(passengerRequest);
    }

    @Test
    public void createPassenger_DuplicatePhone_ExceptionThrown() {
        String phone = "+375111111119";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request3@test.com", "");
        passengerRequest.setPhone(phone);

        PassengerServiceClient.postPassengerWithDuplicate(passengerRequest);
    }

    @Test
    public void deletePassenger_ExistingId_Success() {
        long passengerId = 5L;

        PassengerServiceClient.deletePassenger(passengerId);
    }

    @Test
    public void deletePassenger_NonExistingId_ExceptionThrown() {
        long passengerId = 999L;

        PassengerServiceClient.deletePassengerNonExists(passengerId);
    }
}