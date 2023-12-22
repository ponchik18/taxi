package com.modsen.end_to_end.step.definitions;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.constants.PassengerServiceTestConstants;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.util.PassengerServiceClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassengerEndToEndStepDefinitions {
    private PassengerRequest createPassengerRequest;
    private PassengerRequest updatedPassengerRequest;
    private long passengerId = 1L;
    private long updatedPassengerId;
    @LocalServerPort
    private Integer port;

    @Given("The passenger-service is running")
    public void givenThePassengerServiceIsRunning() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH;
    }

    @Given("The passenger data with first name {string}, last name {string}, email {string}, and phone {string}")
    public void givenThePassengerData(String firstName, String lastName, String email, String phone) {
        createPassengerRequest = new PassengerRequest(firstName, lastName, email, phone);
    }


    @When("The client adds a new passenger")
    public void whenCreateNewPassenger() {
        PassengerServiceClient.postPassenger(createPassengerRequest);
    }

    @Then("The response should contain the new passenger details")
    public void thenResponseShouldContainTheNewPassengerDetails() {
        Response actualResponse = PassengerServiceClient.getPassenger(passengerId);

        PassengerResponse actualPassengerResponse = actualResponse.then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponse.class);
        assertThat(actualPassengerResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createPassengerRequest);

    }

    @Given("The passenger ID is {long}")
    public void thePassengerIDIs(long passengerId) {
        this.passengerId = passengerId;
    }

    @Sql(scripts = "classpath:data/insert-test-data.sql")
    @When("The client deletes the passenger by ID")
    public void whenDeletePassengerById() {
        PassengerServiceClient.postPassenger(new PassengerRequest("Test", "Test", PassengerServiceTestConstants.TestData.EMAIL, PassengerServiceTestConstants.TestData.PHONE));

        Response actualResponse = PassengerServiceClient.deletePassenger(passengerId);
        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Then("The response should indicate a successful deletion")
    public void theResponseShouldIndicateASuccessfulDeletion() {
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, passengerId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = PassengerServiceClient.getPassenger(passengerId);

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

    @When("The client updates the passenger data first name {string}, last name {string}, email {string}, and phone {string}")
    public void UpdatePassengerDetails(String firstName, String lastName, String email, String phone) {
        updatedPassengerId = PassengerServiceClient.postPassenger(createPassengerRequest)
                .as(PassengerResponse.class)
                .id();
        updatedPassengerRequest = new PassengerRequest(firstName, lastName, email, phone);
        PassengerServiceClient.updatePassenger(updatedPassengerId, updatedPassengerRequest);
    }

    @Then("The response should contain the updated passenger details")
    public void thenResponseShouldContainTheUpdatedPassengerDetails() {
        Response actualResponse = PassengerServiceClient.getPassenger(updatedPassengerId);

        PassengerResponse actualPassengerResponse = actualResponse.then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponse.class);
        assertThat(actualPassengerResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(updatedPassengerRequest);
    }

    @When("The client requests the passenger by ID")
    public void whenGetPassengerById() {

    }

    @Then("The response should return {int}")
    public void theResponseShouldReturn(int expectedStatus) {
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, passengerId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = PassengerServiceClient.getPassenger(passengerId);

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