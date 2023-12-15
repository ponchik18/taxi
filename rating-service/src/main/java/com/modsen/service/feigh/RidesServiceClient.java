package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.constants.RatingServiceConstants;
import com.modsen.dto.entities.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = RatingServiceConstants.Path.RIDES_SERVICE_NAME,
        configuration = FeignClientConfiguration.class,
        path = RatingServiceConstants.Path.RIDES_SERVICE_PATH)
public interface RidesServiceClient {
    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable long id);
}