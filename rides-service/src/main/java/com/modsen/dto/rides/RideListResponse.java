package com.modsen.dto.rides;

import lombok.Builder;

import java.util.List;

@Builder
public record RideListResponse(
        List<RideResponse> rides,
        int ridesCount
) {
}