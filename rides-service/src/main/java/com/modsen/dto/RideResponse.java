package com.modsen.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record RideResponse(
        Long id,
        Long passengerId,
        Long driverId,
        String pickUpLocation,
        String dropLocation,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal cost,
        String status
) {
}