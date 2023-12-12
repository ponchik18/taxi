package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;

import com.modsen.dto.entities.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service", configuration = FeignClientConfiguration.class, path = "/api/v1/driver")
public interface DriverServiceClient {
    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable long id);
}