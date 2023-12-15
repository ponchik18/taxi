package com.modsen.exception;

import com.modsen.constants.RidesServiceConstants;
import lombok.Getter;

@Getter
public class RideNotFoundException extends RuntimeException {
    private final long id;
    public RideNotFoundException(long id) {
        super(String.format(RidesServiceConstants.Errors.Message.RIDE_NOT_FOUND, id));
        this.id = id;
    }
}