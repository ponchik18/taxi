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
        value = EndToEndTestConstant.ServiceName.DRIVER_SERVICE_NAME,
        url = "${feign-client.driver-service.url}",
        path = EndToEndTestConstant.ServiceBasePath.DRIVER_SERVICE_PATH)
public interface DriverServiceClient {

    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable long id);

    @PostMapping
    DriverResponse createDriver(@RequestBody DriverRequest driverRequest);
}