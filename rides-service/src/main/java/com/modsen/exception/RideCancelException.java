package com.modsen.exception;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.enums.RideStatus;

public class RideCancelException extends RuntimeException {
    public RideCancelException(Long rideId, RideStatus status) {
        super(String.format(RidesServiceConstants.Errors.Message.RIDE_CAN_NOT_BE_CANCELED, rideId, status));
    }
}