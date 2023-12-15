package com.modsen.dto.entities;

import lombok.Builder;

@Builder
public record DriverResponse(
        Integer id,
        String licenseNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        String driverStatus
) {
}