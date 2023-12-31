package com.modsen.service;

import com.modsen.dto.passenger.PassengerResponse;

import java.util.Optional;

public interface PassengerServiceWebClient {
    Optional<PassengerResponse> getPassengerById(long passengerId);
}