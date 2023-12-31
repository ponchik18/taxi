package com.modsen.service.impl;

import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.enums.UserRole;
import com.modsen.service.PaymentServiceWebClient;
import com.modsen.util.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceWebClientImpl implements PaymentServiceWebClient {

    private final WebClient paymentServiceWebClient;

    @Override
    public void makeCharge(PaymentRequest paymentRequest) {
        paymentServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/charge")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paymentRequest))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    @Override
    public Optional<CreditCardResponse> getDefaultCardForPassenger(long passengerId) {
        return Optional.ofNullable(paymentServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/card/default")
                        .queryParam("userId", passengerId)
                        .queryParam("userRole", UserRole.PASSENGER.name())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(CreditCardResponse.class)
                .block());
    }
}