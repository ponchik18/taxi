package com.modsen.dto.rides;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideCancelRequest {
    private Long rideId;
    private Long passengerId;
}