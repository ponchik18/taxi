package com.modsen.service.impl;

import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;
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
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PromoCodeServiceWebClientImplTest {

    private PromoCodeServiceWebClientImpl promoCodeServiceWebClient;
    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(server.url("/").toString())
                .build();
        promoCodeServiceWebClient = new PromoCodeServiceWebClientImpl(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void applyPromoCodeForRide_ValidPromoCodeApplyRequest_Success() {
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest();
        promoCodeApplyRequest.setPromoCode(RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        PromoCodeResponse expectedPromoCodeResponse = PromoCodeResponse.builder()
                .id(5L)
                .name(promoCodeApplyRequest.getPromoCode())
                .endDate(LocalDate.now())
                .fromDate(LocalDate.now())
                .countOfUse(2)
                .discount(RidesServiceTestConstants.TestData.PROMO_CODE_DISCOUNT)
                .build();
        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(ObjectJsonMapper.convertToJson(expectedPromoCodeResponse))
        );

        PromoCodeResponse actualPromoCodeResponse = promoCodeServiceWebClient.applyPromoCodeForRide(promoCodeApplyRequest)
                .orElse(null);

        assertNotNull(actualPromoCodeResponse);
        assertThat(actualPromoCodeResponse)
                .isEqualTo(expectedPromoCodeResponse);
    }

    @Test
    void applyPromoCodeForRide_InvalidPromoCodeApplyRequest_Success() {
        server.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(RidesServiceTestConstants.Properties.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("{}")
        );

        assertThatThrownBy(() -> promoCodeServiceWebClient.applyPromoCodeForRide(new PromoCodeApplyRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}