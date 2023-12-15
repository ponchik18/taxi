package com.modsen.exception;

import com.modsen.enums.RideStatus;

public class RideCancelException extends RuntimeException {
    public RideCancelException(Long rideId, RideStatus status) {
        super(String.format("Ride with id = '%d' can't be cancel because of status '%s'", rideId, status));
    }
}