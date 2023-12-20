package com.modsen.controller;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.util.PassengerServiceClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

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
        Response actualResponse = PassengerServiceClient.getAllPassengers();

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("passengerCount", greaterThan(0));
    }

    @Test
    public void getPassengerById_ValidId_Success() {
        long passengerId = 9L;

        Response actualResponse = PassengerServiceClient.getPassenger(passengerId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .body("id", equalTo(Long.valueOf(passengerId).intValue()));
    }

    @Test
    public void createPassenger_ValidRequest_Success() {
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request45@test.com", "+375111781178");

        Response actualresponse = PassengerServiceClient.postPassenger(passengerRequest);

        PassengerResponse actualPassenger = actualresponse.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .extract()
                .as(PassengerResponse.class);
        assertThat(actualPassenger)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(passengerRequest);
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

        Response actualResponse = PassengerServiceClient.updatePassenger(passengerId, passengerRequest);

        PassengerResponse actual = actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .extract()
                .body()
                .as(PassengerResponse.class);
        assertThat(actual).isEqualTo(expectedPassenger);
    }

    @Test
    public void updatePassenger_InValidRequest_BadRequest() {
        long passengerId = 1L;
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Response actualResponse = PassengerServiceClient.updatePassenger(passengerId, new PassengerRequest());

        actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .body("statusCode", equalTo(expectedStatus));
    }

    @Test
    public void createPassenger_DuplicateEmail_ExceptionThrown() {
        String email = "john.doe@example.com";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "", "+375111114578");
        passengerRequest.setEmail(email);
        int expectedStatus = HttpStatus.CONFLICT.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL, email))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = PassengerServiceClient.postPassenger(passengerRequest);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);
    }

    @Test
    public void createPassenger_DuplicatePhone_ExceptionThrown() {
        String phone = "+375111111119";
        PassengerRequest passengerRequest = new PassengerRequest("Test", "Test", "test_request3@test.com", "");
        passengerRequest.setPhone(phone);

        int expectedStatus = HttpStatus.CONFLICT.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE, phone))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = PassengerServiceClient.postPassenger(passengerRequest);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);


    }

    @Test
    public void deletePassenger_ExistingId_Success() {
        long passengerId = 5L;

        Response actualResponse = PassengerServiceClient.deletePassenger(passengerId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deletePassenger_NonExistingId_ExceptionThrown() {
        long passengerId = 999L;
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, passengerId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = PassengerServiceClient.deletePassenger(passengerId);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);

    }
}