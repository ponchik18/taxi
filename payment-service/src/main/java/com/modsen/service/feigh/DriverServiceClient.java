package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.users.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = PaymentServiceConstants.BasePath.DRIVER_SERVICE_NAME,
        configuration = FeignClientConfiguration.class,
        path = PaymentServiceConstants.BasePath.DRIVER_SERVICE_PATH)
public interface DriverServiceClient {
    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable long id);
}