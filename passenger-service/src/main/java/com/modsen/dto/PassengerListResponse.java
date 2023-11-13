package com.modsen.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PassengerListResponse(
        List<PassengerResponse> passengers,
        int totalPassengerCount
) {
}