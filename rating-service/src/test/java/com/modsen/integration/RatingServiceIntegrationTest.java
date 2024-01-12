package com.modsen.integration;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.constants.RatingServiceTestConstants;
import com.modsen.dto.entities.DriverResponse;
import com.modsen.dto.entities.PassengerResponse;
import com.modsen.dto.entities.RideResponse;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.repository.RatingRepository;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.modsen.service.feigh.RidesServiceClient;
import com.modsen.util.RatingServiceClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RatingServiceIntegrationTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private RatingRepository ratingRepository;
    @MockBean
    private DriverServiceClient driverServiceClient;
    @MockBean
    private RidesServiceClient ridesServiceClient;
    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = RatingServiceConstants.Path.RATING_CONTROLLER_PATH;
        when(driverServiceClient.getDriverById(anyLong()))
                .thenReturn(DriverResponse.builder().build());
        when(ridesServiceClient.getRideById(anyLong()))
                .thenReturn(RideResponse.builder().build());
        when(passengerServiceClient.getPassengerById(anyLong()))
                .thenReturn(PassengerResponse.builder().build());
    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void getAllDrivers_ValidRequest_Success() {
        Response actualResponse = RatingServiceClient.getAllRating();

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("totalMark", greaterThan(0.0f));
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void getDriverById_ValidId_Success() {
        long ratingId = ratingRepository.findAll()
                .get(0)
                .getId();

        Response actualResponse = RatingServiceClient.getRating(ratingId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void createDriver_ValidRequest_Success() {
        RatingRequest ratingRequest = new RatingRequest(RatingServiceTestConstants.TestData.ENTITY_ID, RatingServiceTestConstants.TestData.MARK, UserRole.PASSENGER.name(), RatingServiceTestConstants.TestData.RIDE_ID);

        Response actualresponse = RatingServiceClient.postRating(ratingRequest);

        RatingResponse actualRatingResponse = actualresponse.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RatingResponse.class);
        assertThat(actualRatingResponse)
                .usingRecursiveComparison()
                .ignoringFields("id", "userRole")
                .isEqualTo(ratingRequest);
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void deleteRating_ExistingId_Success() {
        long ratingId = ratingRepository.findAll()
                .get(0)
                .getId();

        Response actualResponse = RatingServiceClient.deleteRating(ratingId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertThat(ratingRepository.existsById(ratingId))
                .isFalse();
    }

    @Test
    public void deleteRating_NonExistingId_ExceptionThrown() {
        long ratingId = 999L;
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(RatingServiceConstants.Errors.Message.RATING_NOT_FOUND, ratingId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = RatingServiceClient.deleteRating(ratingId);

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