package com.modsen.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient passengerServiceWebClient() {
        return webClientBuilder().baseUrl("http://passenger-service/api/v1/passenger").build();
    }

    @Bean
    public WebClient paymentServiceWebClient() {
        return webClientBuilder().baseUrl("http://payment-service/api/v1/payment").build();
    }

    @Bean
    public WebClient promoCodeServiceWebClient() {
        return webClientBuilder().baseUrl("http://promo-code-service/api/v1/promo-code").build();
    }
}