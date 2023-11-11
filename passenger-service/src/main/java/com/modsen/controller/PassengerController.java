package com.modsen.controller;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.PassengerRequest;
import com.modsen.dto.PassengerResponse;
import com.modsen.model.PageSetting;
import com.modsen.service.PassengerService;
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

import java.util.List;

@RestController
@RequestMapping(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH)
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerResponse> getAllPassenger(PageSetting pageSetting) {
        return passengerService.getAllPassenger(pageSetting);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        passengerService.createPassenger(passengerRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse getPassengerById(@PathVariable Integer id) {
        return passengerService.getPassengerById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassenger(@PathVariable Integer id, @Valid @RequestBody PassengerRequest passengerRequest) {
        passengerService.updatePassenger(id, passengerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable Integer id) {
        passengerService.deletePassenger(id);
    }
}