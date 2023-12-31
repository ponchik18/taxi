package com.modsen.service.impl;

import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.enums.UserRole;
import com.modsen.util.ObjectJsonMapper;
import jakarta.persistence.EntityNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentServiceWebClientImplTest {

    private PaymentServiceWebClientImpl paymentServiceWebClient;
    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(server.url("/").toString())
                .build();
        paymentServiceWebClient = new PaymentServiceWebClientImpl(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void getDefaultCardForPassenger_ExistingUser_Success() {
        long passengerId = 1;
        CreditCardResponse expectedCreditCardResponse = CreditCardResponse.builder()
                .id(5L)
                .cardNumber("4444444444444444")
                .cardHolder("Test Test")
                .userId(passengerId)
                .userRole(UserRole.PASSENGER)
                .customerId("cus_000000")
                .build();
        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(ObjectJsonMapper.convertToJson(expectedCreditCardResponse))
        );

        CreditCardResponse actualCreditCardResponse = paymentServiceWebClient.getDefaultCardForPassenger(passengerId)
                .orElse(null);

        assertNotNull(actualCreditCardResponse);
        assertThat(actualCreditCardResponse)
                .isEqualTo(expectedCreditCardResponse);
    }

    @Test
    public void getDefaultCardForPassenger_UnExistingUser_ThrowException() {
        int passengerId = 999;

        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("{}")
        );

        assertThatThrownBy(() -> paymentServiceWebClient.getDefaultCardForPassenger(passengerId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void makeCharge_ValidPaymentRequest_Success() {
        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.CREATED.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("{}")
        );

        assertDoesNotThrow(() -> paymentServiceWebClient.makeCharge(new PaymentRequest()));
    }

}