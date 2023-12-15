package com.modsen.contoller;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideListResponse;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.model.PageSetting;
import com.modsen.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RidesServiceConstants.Path.RIDES_CONTROLLER_PATH)
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RideListResponse getAllRide(PageSetting pageSetting, @RequestParam(required = false) Long passengerId) {
        return rideService.getAllRide(pageSetting, passengerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse getRideById(@PathVariable long id) {
        return rideService.getRideById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRide(@Valid @RequestBody RidePassengerRequest ridePassengerRequest) {
        rideService.createRide(ridePassengerRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse updateRide(@PathVariable long id, @Valid @RequestBody RideDriverRequest rideDriverRequest) {
        return rideService.updateRide(id, rideDriverRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable long id) {
        rideService.deleteRide(id);
    }

    @PutMapping("/finish")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse finishRide(@Valid @RequestBody ChangeRideStatusRequest changeRideStatusRequest) {
        return rideService.finishRide(changeRideStatusRequest);
    }

    @PutMapping("/ride")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse cancelRide(@Valid @RequestBody ChangeRideStatusRequest changeRideStatusRequest) {
        return rideService.cancelRide(changeRideStatusRequest);
    }

    @PutMapping("/arrived")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse confirmDriverArrival(@Valid @RequestBody ChangeRideStatusRequest changeRideStatusRequest) {
        return rideService.confirmDriverArrival(changeRideStatusRequest);
    }

    @PutMapping("/promo-code")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse applyPromoCode(@Valid @RequestBody PromoCodeApplyRequest promoCodeApplyRequest) {
        return rideService.applyApplyCode(promoCodeApplyRequest);
    }
}