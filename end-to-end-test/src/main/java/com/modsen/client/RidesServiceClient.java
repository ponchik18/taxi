package com.modsen.client;

import com.modsen.constant.EndToEndTestConstant;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = EndToEndTestConstant.ServiceName.RIDES_SERVICE_NAME,
        url = "${feign-client.rides-service.url}",
        path = EndToEndTestConstant.ServiceBasePath.RIDES_SERVICE_PATH)
public interface RidesServiceClient {

    @GetMapping
    RideResponse getRideById(@PathVariable long id);

    @PostMapping
    void createRide(@RequestBody RidePassengerRequest ridePassengerRequest);

    @PutMapping("/arrived")
    RideResponse confirmDriverArrival(@RequestBody ChangeRideStatusRequest changeRideStatusRequest);

    @PutMapping("/finish")
    RideResponse finishRide(@RequestBody ChangeRideStatusRequest changeRideStatusRequest);

    @PutMapping("/promo-code")
    RideResponse applyPromoCode(@RequestBody PromoCodeApplyRequest promoCodeApplyRequest);
}