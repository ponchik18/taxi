package com.modsen.integration;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.enums.RideStatus;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import com.modsen.service.PassengerServiceWebClient;
import com.modsen.service.PaymentServiceWebClient;
import com.modsen.service.PromoCodeServiceWebClient;
import com.modsen.util.RidesServiceClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = RidesServiceIntegrationTestConfiguration.class)
public class RidesServiceIntegrationTest extends TestBaseContainer {

    @LocalServerPort
    private Integer port;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private KafkaConsumer<Object, Object> testKafkaConsumer;
    @MockBean
    private PassengerServiceWebClient passengerServiceWebClient;
    @MockBean
    private PromoCodeServiceWebClient promoCodeServiceWebClient;
    @MockBean
    private PaymentServiceWebClient paymentServiceWebClient;
    @Value("${spring.integration.kafka.sent-topic}")
    private String springIntegrationKafkaAcceptedTopic;


    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = RidesServiceConstants.Path.RIDES_CONTROLLER_PATH;
    }

    @AfterEach
    void tearDown() {
        rideRepository.deleteAll();
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void getAllDrivers_ValidRequest_Success() {
        Response actualResponse = RidesServiceClient.getAllRide();

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("ridesCount", greaterThan(0));
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void getRideById_ValidId_Success() {
        long rideId = rideRepository.findAll()
                .get(0)
                .getId();

        Response actualResponse = RidesServiceClient.getRide(rideId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(Long.valueOf(rideId).intValue()));
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void createRide_ValidRequest_Success() {
        RidePassengerRequest ridePassengerRequest = new RidePassengerRequest(RidesServiceTestConstants.TestData.PASSENGER_ID, "loc1", "loc2");
        testKafkaConsumer.subscribe(Collections.singletonList(springIntegrationKafkaAcceptedTopic));

        when(passengerServiceWebClient.getPassengerById(anyLong()))
                .thenReturn(Optional.of(PassengerResponse.builder().build()));
        when(paymentServiceWebClient.getDefaultCardForPassenger(anyLong()))
                .thenReturn(Optional.of(CreditCardResponse.builder().build()));
        Response actualresponse = RidesServiceClient.postRide(ridePassengerRequest);

        actualresponse.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
        ConsumerRecords<Object, Object> records = testKafkaConsumer.poll(Duration.ofSeconds(5));
        assertThat(records.count())
                .isEqualTo(1);
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void updateRide_ValidIdAndRequest_Success() {
        long rideId = rideRepository.findAll()
                .get(0)
                .getId();
        RideDriverRequest rideDriverRequest = new RideDriverRequest(RidesServiceTestConstants.TestData.PASSENGER_ID, "loc1", "loc2", LocalDateTime.now(), LocalDateTime.now(), BigDecimal.TEN, RideStatus.COMPLETED.name());
        when(passengerServiceWebClient.getPassengerById(anyLong()))
                .thenReturn(Optional.of(PassengerResponse.builder().build()));

        RideResponse expectedRideResponse = RideResponse.builder()
                .id(rideId)
                .passengerId(rideDriverRequest.getPassengerId())
                .pickUpLocation(rideDriverRequest.getPickUpLocation())
                .dropLocation(rideDriverRequest.getDropLocation())
                .status(rideDriverRequest.getStatus())
                .startTime(rideDriverRequest.getStartTime())
                .endTime(rideDriverRequest.getEndTime())
                .cost(BigDecimal.valueOf(rideDriverRequest.getCost().doubleValue()))
                .build();

        RidesServiceClient.updateRide(rideId, rideDriverRequest);

        Ride actualRide = rideRepository.findById(rideId)
                .orElseThrow();
        assertThat(actualRide).usingRecursiveComparison()
                .ignoringFields("id", "status", "endTime", "startTime", "cost")
                .isEqualTo(expectedRideResponse);
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void updateRide_InValidRequest_BadRequest() {
        long rideId = rideRepository.findAll()
                .get(0)
                .getId();
        int expectedStatus = HttpStatus.BAD_REQUEST.value();
        when(passengerServiceWebClient.getPassengerById(anyLong()))
                .thenReturn(Optional.of(PassengerResponse.builder().build()));

        Response actualResponse = RidesServiceClient.updateRide(rideId, new RideDriverRequest());

        actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .body("statusCode", equalTo(expectedStatus));
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void deleteDriver_ExistingId_Success() {
        long rideId = rideRepository.findAll()
                .get(0)
                .getId();

        Response actualResponse = RidesServiceClient.deleteRide(rideId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertThat(rideRepository.existsById(rideId))
                .isFalse();
    }

    @Test
    public void deleteDriver_NonExistingId_ExceptionThrown() {
        long rideId = 9999L;
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(RidesServiceConstants.Errors.Message.RIDE_NOT_FOUND, rideId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = RidesServiceClient.deleteRide(rideId);

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
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void applyPromoCode_ExistingPromoCode_Success() {
        Ride ride = rideRepository.findAll()
                .get(0);
        long rideId = ride.getId();
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(rideId, ride.getPassengerId(), RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        BigDecimal expectedCost = new BigDecimal("80.00");

        PromoCodeResponse promoCodeResponse = PromoCodeResponse.builder()
                .discount(RidesServiceTestConstants.TestData.PROMO_CODE_DISCOUNT)
                .name(RidesServiceTestConstants.TestData.PROMO_CODE_NAME)
                .build();
        when(promoCodeServiceWebClient.applyPromoCodeForRide(any()))
                .thenReturn(Optional.of(promoCodeResponse));
        Response actualResponse = RidesServiceClient.applyPromoCode(promoCodeApplyRequest);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
        Ride expectedRide = rideRepository.findById(rideId)
                .orElseThrow();
        assertThat(expectedRide.getCost())
                .isEqualTo(expectedCost);
    }

    @Test
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void applyPromoCode_UnExistingPromoCode_ThrowException() {
        Ride ride = rideRepository.findAll()
                .get(0);
        long rideId = ride.getId();
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(rideId, ride.getPassengerId(), RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(RidesServiceConstants.Errors.Message.PROMO_CODE_NOT_FOUND, promoCodeApplyRequest.getPromoCode()))
                .statusCode(expectedStatus)
                .build();

        when(promoCodeServiceWebClient.applyPromoCodeForRide(any()))
                .thenReturn(Optional.empty());

        Response actualResponse = RidesServiceClient.applyPromoCode(promoCodeApplyRequest);

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
    @Sql(scripts = "classpath:data/insert-test-data.sql")
    public void cancelRide_ValidRideAndChangeRideStatusRequest_Success() {
        Ride ride = rideRepository.findAll()
                .get(1);
        ride.setStatus(RideStatus.DRIVER_EN_ROUTE);
        rideRepository.save(ride);
        long rideId = ride.getId();
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(rideId, ride.getDriverId());

        Response actualResponse = RidesServiceClient.cancelRide(changeRideStatusRequest);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
        Ride expectedRide = rideRepository.findById(rideId)
                .orElseThrow();
        assertThat(expectedRide.getStatus())
                .isEqualTo(RideStatus.TRIP_CANCELED);
    }
}