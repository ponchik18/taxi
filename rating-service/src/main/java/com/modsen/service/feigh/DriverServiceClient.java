package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.constants.RatingServiceConstants;
import com.modsen.dto.entities.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = RatingServiceConstants.Path.DRIVER_SERVICE_NAME,
        configuration = FeignClientConfiguration.class,
        path = RatingServiceConstants.Path.DRIVER_SERVICE_PATH)
public interface DriverServiceClient {
    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable long id);
}