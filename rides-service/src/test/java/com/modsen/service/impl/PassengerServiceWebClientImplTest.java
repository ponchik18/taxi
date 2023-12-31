package com.modsen.service.impl;

import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.passenger.PassengerResponse;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PassengerServiceWebClientImplTest {

    private PassengerServiceWebClientImpl passengerServiceWebClient;
    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(server.url("/").toString())
                .build();
        passengerServiceWebClient = new PassengerServiceWebClientImpl(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void getPassengerById_ExistingId_Success() {
        int passengerId = 1;
        PassengerResponse expectedPassengerResponse = PassengerResponse.builder()
                .id(passengerId)
                .email(RidesServiceTestConstants.TestData.EMAIL)
                .phone(RidesServiceTestConstants.TestData.PHONE)
                .firstName(RidesServiceTestConstants.TestData.FIRST_NAME)
                .lastName(RidesServiceTestConstants.TestData.LAST_NAME)
                .build();
        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(ObjectJsonMapper.convertToJson(expectedPassengerResponse))
        );
        PassengerResponse actualPassengerResponse = passengerServiceWebClient.getPassengerById(passengerId)
                .orElse(null);

        assertNotNull(actualPassengerResponse);
        assertThat(actualPassengerResponse)
                .isEqualTo(expectedPassengerResponse);
    }

    @Test
    void getPassengerById_UnExistingId_ThrowException() {
        int passengerId = 999;

        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("{}")
        );

        assertThatThrownBy(() -> passengerServiceWebClient.getPassengerById(passengerId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}