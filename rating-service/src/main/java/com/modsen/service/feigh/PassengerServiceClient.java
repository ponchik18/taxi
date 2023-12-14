package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.constants.RatingServiceConstants;
import com.modsen.dto.entities.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = RatingServiceConstants.Path.PASSENGER_SERVICE_NAME,
        configuration = FeignClientConfiguration.class,
        path = RatingServiceConstants.Path.PASSENGER_SERVICE_PATH)
public interface PassengerServiceClient {
    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable long id);
}