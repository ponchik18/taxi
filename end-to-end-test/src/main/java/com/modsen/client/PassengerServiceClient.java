package com.modsen.client;

import com.modsen.constant.EndToEndTestConstant;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = EndToEndTestConstant.ServiceName.PASSENGER_SERVICE_NAME,
        url = "${feign-client.passenger-service.url}",
        path = EndToEndTestConstant.ServiceBasePath.PASSENGER_SERVICE_PATH)
public interface PassengerServiceClient {

    @GetMapping("/{id}")
    DriverResponse getPassengerId(@PathVariable long id);

    @PostMapping
    DriverResponse createPassenger(@RequestBody DriverRequest driverRequest);
}