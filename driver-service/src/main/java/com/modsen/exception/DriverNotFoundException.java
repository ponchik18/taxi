package com.modsen.exception;

import com.modsen.constants.DriverServiceConstants;
import lombok.Getter;

@Getter
public class DriverNotFoundException extends RuntimeException {
    private final long id;

    public DriverNotFoundException(long id) {
        super(String.format(DriverServiceConstants.Errors.Message.DRIVER_NOT_FOUND, id));
        this.id = id;
    }
}