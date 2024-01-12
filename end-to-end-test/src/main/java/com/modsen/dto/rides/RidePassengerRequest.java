package com.modsen.dto.rides;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidePassengerRequest {
    private Long passengerId;
    private String pickUpLocation;
    private String dropLocation;
}