package com.modsen.service.impl;

import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.service.PassengerServiceWebClient;
import com.modsen.util.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerServiceWebClientImpl implements PassengerServiceWebClient {

    private final WebClient passengerServiceWebClient;

    @Override
    public Optional<PassengerResponse> getPassengerById(long passengerId) {
        return Optional.ofNullable(passengerServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{id}")
                        .build(passengerId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(PassengerResponse.class)
                .block());
    }
}