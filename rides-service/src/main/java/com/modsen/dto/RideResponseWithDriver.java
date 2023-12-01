package com.modsen.dto;

import lombok.Builder;

@Builder
public record RideResponseWithDriver(
        RideRequest rideRequest,
        Long driverId
) {

}