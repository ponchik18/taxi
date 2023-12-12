package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.dto.entities.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "rides-service", configuration = FeignClientConfiguration.class, path = "/api/v1/rides")
public interface RidesServiceClient {
    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable long id);
}