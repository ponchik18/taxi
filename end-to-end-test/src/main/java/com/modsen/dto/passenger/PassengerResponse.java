package com.modsen.dto.passenger;

import lombok.Builder;

@Builder
public record PassengerResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phone
) { }