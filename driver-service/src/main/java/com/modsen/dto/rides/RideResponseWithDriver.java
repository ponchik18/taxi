package com.modsen.dto.rides;

import lombok.Builder;

@Builder
public record RideResponseWithDriver(
        RideDriverRequest rideDriverRequest,
        Long driverId
) {

}