package com.modsen.dto.driver;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverListResponse(
        List<DriverResponse> drivers,
        int driverCount

) {
}