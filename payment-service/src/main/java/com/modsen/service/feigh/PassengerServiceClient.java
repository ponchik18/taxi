package com.modsen.service.feigh;

import com.modsen.config.FeignClientConfiguration;
import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.users.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = PaymentServiceConstants.BasePath.PASSENGER_SERVICE_NAME,
        configuration = FeignClientConfiguration.class,
        path = PaymentServiceConstants.BasePath.PASSENGER_SERVICE_PATH)
public interface PassengerServiceClient {
    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable long id);
}