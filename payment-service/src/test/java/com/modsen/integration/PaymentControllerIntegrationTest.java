package com.modsen.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.constants.PaymentServiceConstants;
import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.dto.users.PassengerResponse;
import com.modsen.enums.UserRole;
import com.modsen.model.StripeCustomer;
import com.modsen.repository.StripeCustomerRepository;
import com.modsen.service.StripeService;
import com.modsen.util.ObjectJsonMapper;
import com.modsen.util.PaymentControllerClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentControllerIntegrationTest extends TestBaseContainer {
    private static WireMockServer driverServiceWireMock;
    private static WireMockServer passengerServiceWireMock;
    @LocalServerPort
    private Integer port;
    @Autowired
    private StripeCustomerRepository stripeCustomerRepository;
    @MockBean
    private StripeService stripeService;

    @BeforeAll
    static void beforeAll() {
        driverServiceWireMock = new WireMockServer(PaymentServiceTestConstants.TestData.DRIVER_SERVICE_PORT);
        driverServiceWireMock.start();
        configureFor(driverServiceWireMock.port());

        passengerServiceWireMock = new WireMockServer(PaymentServiceTestConstants.TestData.PASSENGER_SERVICE_PORT);
        passengerServiceWireMock.start();
        configureFor(passengerServiceWireMock.port());
    }

    @AfterAll
    static void afterAll() {
        driverServiceWireMock.stop();
        passengerServiceWireMock.stop();
    }

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = PaymentServiceConstants.BasePath.PAYMENT_CONTROLLER_PATH;
    }

    @Test
    @Order(2)
    public void charge_ValidPaymentRequest_Success() {
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.TEN, PaymentServiceTestConstants.TestData.PASSENGER_ID, PaymentServiceTestConstants.TestData.RIDE_ID, PaymentServiceTestConstants.TestData.DRIVER_ID);
        String url = PaymentServiceConstants.BasePath.PASSENGER_SERVICE_PATH + "/" + paymentRequest.getPassengerId();
        StripeCustomer stripeCustomer = StripeCustomer.builder()
                .customerId(PaymentServiceTestConstants.TestData.CUSTOMER_ID)
                .userRole(UserRole.PASSENGER)
                .userId(PaymentServiceTestConstants.TestData.PASSENGER_ID)
                .build();
        stripeCustomerRepository.save(stripeCustomer);
        PaymentResponse expectedPaymentResponse = PaymentResponse.builder()
                .amount(paymentRequest.getAmount())
                .rideId(PaymentServiceTestConstants.TestData.RIDE_ID)
                .build();
        int expectedStatus = HttpStatus.CREATED.value();

        stubFor(get(urlEqualTo(url))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(ObjectJsonMapper.convertToJson(PassengerResponse.builder().build()))));

        Response actualResponse = PaymentControllerClient.postCharge(paymentRequest);

        PaymentResponse actualPaymentResponse = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(PaymentResponse.class);
        assertThat(actualPaymentResponse).usingRecursiveComparison()
                .ignoringFields("id", "paymentDate")
                .isEqualTo(expectedPaymentResponse);
        verify(stripeService, times(1))
                .charge(any(), any());
    }

    @Test
    @Order(3)
    public void getPaymentHistory_ExistPayments_Success() {
        int expectedStatus = HttpStatus.OK.value();

        Response actualResponse = PaymentControllerClient.getPaymentHistory();

        actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .body("countOfPayment", greaterThan(0));
    }
}