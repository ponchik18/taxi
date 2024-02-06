package com.modsen.config;

import com.modsen.constants.RidesServiceConstants;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient
                .builder()
                .filter(addBearerTokenExchangeFunction());
    }

    @Bean
    public WebClient passengerServiceWebClient() {
        return webClientBuilder()
                .baseUrl(RidesServiceConstants.Path.PASSENGER_SERVICE_PATH)
                .build();
    }

    @Bean
    public WebClient paymentServiceWebClient() {
        return webClientBuilder()
                .baseUrl(RidesServiceConstants.Path.PAYMENT_SERVICE_PATH)
                .build();
    }

    @Bean
    public WebClient promoCodeServiceWebClient() {
        return webClientBuilder()
                .baseUrl(RidesServiceConstants.Path.PROMO_CODE_SERVICE_PATH)
                .build();
    }

    @Bean
    public ExchangeFilterFunction addBearerTokenExchangeFunction() {
        return (clientRequest, next) -> {
            final String authorization = HttpHeaders.AUTHORIZATION;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
                clientRequest.headers().add(authorization, "Bearer " + tokenValue);
            }
            return next.exchange(clientRequest);
        };
    }
}