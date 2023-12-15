package com.modsen.dto.users;

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