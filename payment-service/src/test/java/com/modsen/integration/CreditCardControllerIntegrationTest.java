package com.modsen.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.constants.PaymentServiceConstants;
import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.card.DefaultCreditCardRequest;
import com.modsen.dto.users.DriverResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.repository.CreditCardRepository;
import com.modsen.util.CreditCardControllerClient;
import com.modsen.util.ObjectJsonMapper;
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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditCardControllerIntegrationTest extends TestBaseContainer {
    private static WireMockServer wireMockServer;
    @LocalServerPort
    private Integer port;
    @Autowired
    private CreditCardRepository creditCardRepository;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(PaymentServiceTestConstants.TestData.DRIVER_SERVICE_PORT);
        wireMockServer.start();
        configureFor(wireMockServer.port());
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = PaymentServiceConstants.BasePath.CARD_CONTROLLER_PATH;
    }

    @Test
    @Order(2)
    public void addCreditCard_ValidCreditCardRequest_Success() {
        CreditCardRequest creditCardRequest = new CreditCardRequest(PaymentServiceTestConstants.TestData.CARD_HOLDER, PaymentServiceTestConstants.TestData.CARD_NUMBER, PaymentServiceTestConstants.TestData.EXP_MONTH, PaymentServiceTestConstants.TestData.EXP_YEAR, PaymentServiceTestConstants.TestData.CVC, UserRole.DRIVER.name(), PaymentServiceTestConstants.TestData.DRIVER_ID);
        String responseBody = ObjectJsonMapper.convertToJson(
                DriverResponse.builder()
                        .email(PaymentServiceTestConstants.TestData.EMAIL)
                        .build()
        );
        String url = PaymentServiceConstants.BasePath.DRIVER_SERVICE_PATH + "/" + creditCardRequest.getUserId();
        long creditCardId;

        stubFor(get(urlEqualTo(url))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        Response actualResponse = CreditCardControllerClient.postCreditCard(creditCardRequest);

        CreditCardResponse creditCardResponse = actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreditCardResponse.class);
        assertThat(creditCardResponse.cardNumber())
                .isEqualTo(creditCardResponse.cardNumber());
        creditCardId = creditCardResponse.id();
        Boolean isCreditCardExist = creditCardRepository.existsById(creditCardId);
        assertThat(isCreditCardExist)
                .isTrue();
    }

    @Test
    @Order(3)
    public void getCreditCardById_ExistId_Success() {
        long creditCardId = creditCardRepository.findAll()
                .get(0)
                .getId();
        Response actualResponse = CreditCardControllerClient.getCreditCard(creditCardId);

        CreditCardResponse creditCardResponse = actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CreditCardResponse.class);
        assertThat(creditCardResponse.id())
                .isEqualTo(creditCardId);
    }

    @Test
    public void getCreditCardById_UnExistId_ThrowException() {
        long creditCardId = 999L;
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedErrorMessage = ErrorMessageResponse.builder()
                .message(String.format(PaymentServiceConstants.Errors.Message.CREDIT_CARD_NOT_ADD, creditCardId))
                .statusCode(expectedStatus)
                .build();
        Response actualResponse = CreditCardControllerClient.getCreditCard(creditCardId);

        ErrorMessageResponse actualErrorMessageResponse = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualErrorMessageResponse).usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedErrorMessage);
    }

    @Test
    @Order(3)
    public void getAllUserCreditCard_ExistUser_Success() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        UserRole userRole = UserRole.DRIVER;
        int expectedStatus = HttpStatus.OK.value();

        Response actualResponse = CreditCardControllerClient.getAllUserCreditCard(userId, userRole.name());

        actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .body("countOfCard", greaterThan(0));
    }

    @Test
    @Order(3)
    public void makeCreditCardDefault_ValidCreditCardRequest_Success() {
        setupForMakeCreditCardDefault();
        long creditCardId = creditCardRepository.findAll()
                .get(1)
                .getId();
        DefaultCreditCardRequest defaultCreditCardRequest = new DefaultCreditCardRequest(PaymentServiceTestConstants.TestData.DRIVER_ID, UserRole.DRIVER.name(), creditCardId);
        int expectedStatus = HttpStatus.OK.value();

        Response actualResponse = CreditCardControllerClient.makeCreditCardDefault(defaultCreditCardRequest);

        CreditCardResponse creditCardResponse = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(CreditCardResponse.class);
        assertThat(creditCardResponse.id())
                .isEqualTo(creditCardId);
    }

    private void setupForMakeCreditCardDefault() {
        CreditCardRequest creditCardRequest = new CreditCardRequest(PaymentServiceTestConstants.TestData.CARD_HOLDER, PaymentServiceTestConstants.TestData.CARD_NUMBER, PaymentServiceTestConstants.TestData.EXP_MONTH, PaymentServiceTestConstants.TestData.EXP_YEAR, PaymentServiceTestConstants.TestData.CVC, UserRole.DRIVER.name(), PaymentServiceTestConstants.TestData.DRIVER_ID);
        String responseBody = ObjectJsonMapper.convertToJson(
                DriverResponse.builder()
                        .email(PaymentServiceTestConstants.TestData.EMAIL)
                        .build()
        );
        String url = PaymentServiceConstants.BasePath.DRIVER_SERVICE_PATH + "/" + creditCardRequest.getUserId();

        stubFor(get(urlEqualTo(url))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        CreditCardControllerClient.postCreditCard(creditCardRequest);
    }

    @Test
    @Order(3)
    public void getDefaultCardForUser_ExistUser_Success() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        UserRole userRole = UserRole.DRIVER;
        long creditCardId = creditCardRepository.findAll()
                .get(0)
                .getId();
        int expectedStatus = HttpStatus.OK.value();

        Response actualResponse = CreditCardControllerClient.getDefaultCardForUser(userId, userRole.name());

        CreditCardResponse creditCardResponse = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(CreditCardResponse.class);
        assertThat(creditCardResponse.id())
                .isEqualTo(creditCardId);
    }

    @Test
    @Order(6)
    public void deleteCreditCard_ExistCreditCard_Success() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        UserRole userRole = UserRole.DRIVER;
        long creditCardId = creditCardRepository.findAll()
                .get(1)
                .getId();
        int expectedStatus = HttpStatus.NO_CONTENT.value();

        CreditCardControllerClient.deleteCreditCard(creditCardId, userId, userRole.name()).then()
                .statusCode(expectedStatus);

        Boolean isCreditCardExist = creditCardRepository.existsById(creditCardId);
        assertThat(isCreditCardExist)
                .isFalse();
    }

    @Test
    @Order(3)
    public void deleteCreditCard_DefaultCardCanNotBeDeleted_ThrowException() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        UserRole userRole = UserRole.DRIVER;
        long creditCardId = creditCardRepository.findAll()
                .get(0)
                .getId();
        int expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorMessageResponse expectedErrorMessageResponse = ErrorMessageResponse.builder()
                .message(String.format(PaymentServiceConstants.Errors.Message.DEFAULT_CREDIT_CARD_DELETION, creditCardId))
                .statusCode(expectedStatus)
                .build();
        ErrorMessageResponse actualErrorMessageResponse = CreditCardControllerClient.deleteCreditCard(creditCardId, userId, userRole.name()).then()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualErrorMessageResponse).usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedErrorMessageResponse);
    }
}