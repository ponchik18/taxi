package com.modsen.dto.passenger;

import lombok.Builder;

import java.util.List;

@Builder
public record PassengerListResponse(
        List<PassengerResponse> passengers,
        int passengerCount
) {
}