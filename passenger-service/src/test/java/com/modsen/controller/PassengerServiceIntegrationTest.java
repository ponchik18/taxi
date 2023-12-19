package com.modsen.controller;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:data/insert-test-data.sql")
public class PassengerServiceIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    public void testGetAllPassenger() {
        ResponseEntity<PassengerListResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH,
                HttpMethod.GET,
                null,
                PassengerListResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).passengerCount()).isGreaterThan(0);
    }

    @Test
    public void testGetPassengerById() {
        long passengerId = 1L;

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId,
                HttpMethod.GET,
                null,
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(passengerId);
    }

    @Test
    public void testCreatePassenger() {
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request45@test.com", "+375111781178");

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH,
                HttpMethod.POST,
                new HttpEntity<>(passengerRequest),
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        PassengerResponse responseBody = response.getBody();
        assert responseBody != null;
        assertThat(responseBody.email()).isEqualTo(passengerRequest.getEmail());
        assertThat(responseBody.phone()).isEqualTo(passengerRequest.getPhone());
        assertThat(responseBody.firstName()).isEqualTo(passengerRequest.getFirstName());
        assertThat(responseBody.lastName()).isEqualTo(passengerRequest.getLastName());
    }

    @Test
    public void testUpdatePassenger() {
        long passengerId = 6L;
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request2@test.com", "+375111114578");

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId,
                HttpMethod.PUT,
                new HttpEntity<>(passengerRequest),
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PassengerResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assert responseBody != null;
        assertThat(responseBody.email()).isEqualTo(passengerRequest.getEmail());
        assertThat(responseBody.phone()).isEqualTo(passengerRequest.getPhone());
        assertThat(responseBody.firstName()).isEqualTo(passengerRequest.getFirstName());
        assertThat(responseBody.lastName()).isEqualTo(passengerRequest.getLastName());
    }

    @Test
    public void testValidationWhenCreateOrUpdatePassenger() {
        long passengerId = 1L;

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId,
                HttpMethod.PUT,
                new HttpEntity<>(new PassengerRequest()),
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testConflictEmailWhenCreateOrUpdatePassenger() {
        String email = "john.doe@example.com";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "", "+375111114578");
        passengerRequest.setEmail(email);

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH,
                HttpMethod.POST,
                new HttpEntity<>(passengerRequest),
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void testConflictPhoneWhenCreateOrUpdatePassenger() {
        String phone = "+375111111119";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request3@test.com", "");
        passengerRequest.setPhone(phone);

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH,
                HttpMethod.POST,
                new HttpEntity<>(passengerRequest),
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void testDeletePassengerById() {
        long passengerId = 5L;

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId,
                HttpMethod.DELETE,
                null,
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testDeletePassengerByIdThrowNotFound() {
        long passengerId = 999L;

        ResponseEntity<PassengerResponse> response = restTemplate.exchange(
                PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId,
                HttpMethod.DELETE,
                null,
                PassengerResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}