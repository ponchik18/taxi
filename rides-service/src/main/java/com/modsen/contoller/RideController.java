package com.modsen.contoller;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.dto.RideListResponse;
import com.modsen.dto.RideRequest;
import com.modsen.dto.RideResponse;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RidesServiceConstants.Path.RIDES_CONTROLLER_PATH)
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RideListResponse getAllRider(PageSetting pageSetting) {
        return rideService.getAllRide(pageSetting);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse getRideById(@PathVariable long id) {
        return rideService.getRideById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponse createRide(@Valid @RequestBody RideRequest rideRequest) {
        return rideService.createRide(rideRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse updateRide(@PathVariable long id, @Valid @RequestBody RideRequest rideRequest) {
        return rideService.updateRide(id, rideRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable long id) {
        rideService.deleteRide(id);
    }
}