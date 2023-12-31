package com.modsen.service.impl;

import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;
import com.modsen.service.PromoCodeServiceWebClient;
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
public class PromoCodeServiceWebClientImpl implements PromoCodeServiceWebClient {
    private final WebClient promoCodeServiceWebClient;

    @Override
    public Optional<PromoCodeResponse> applyPromoCodeForRide(PromoCodeApplyRequest promoCodeApplyRequest) {
        return Optional.ofNullable(promoCodeServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/apply/{name}")
                        .build(promoCodeApplyRequest.getPromoCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(BodyInserters.fromValue(promoCodeApplyRequest))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(PromoCodeResponse.class)
                .block());
    }
}