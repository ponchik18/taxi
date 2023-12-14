package com.modsen.config;

import com.modsen.constants.RidesServiceConstants;
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
        return webClientBuilder().baseUrl(RidesServiceConstants.Path.PASSENGER_SERVICE_PATH).build();
    }

    @Bean
    public WebClient paymentServiceWebClient() {
        return webClientBuilder().baseUrl(RidesServiceConstants.Path.PAYMENT_SERVICE_PATH).build();
    }

    @Bean
    public WebClient promoCodeServiceWebClient() {
        return webClientBuilder().baseUrl(RidesServiceConstants.Path.PROMO_CODE_SERVICE_PATH).build();
    }
}