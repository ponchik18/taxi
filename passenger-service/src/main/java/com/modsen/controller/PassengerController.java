package com.modsen.controller;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.model.PageSetting;
import com.modsen.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH)
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PassengerListResponse getAllPassenger(PageSetting pageSetting) {
        return passengerService.getAllPassenger(pageSetting);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PASSENGER')")
    public PassengerResponse createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        return passengerService.createPassenger(passengerRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PASSENGER')")
    public PassengerResponse getPassengerById(@PathVariable long id) {
        return passengerService.getPassengerById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PASSENGER')")
    public PassengerResponse updatePassenger(@PathVariable long id, @Valid @RequestBody PassengerRequest passengerRequest) {
        return passengerService.updatePassenger(id, passengerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePassenger(@PathVariable long id) {
        passengerService.deletePassenger(id);
    }
}