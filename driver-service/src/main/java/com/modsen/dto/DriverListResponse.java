package com.modsen.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverListResponse(
        List<DriverResponse> drivers,
        int totalDriversCount

) {
}