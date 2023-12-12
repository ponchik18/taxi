package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.dto.users.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", configuration = FeignClientConfiguration.class, path = "/api/v1/passenger")
public interface PassengerServiceClient {
    @GetMapping("/{id}")

    PassengerResponse getPassengerById(@PathVariable long id);
}