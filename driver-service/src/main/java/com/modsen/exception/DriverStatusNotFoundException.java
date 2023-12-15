package com.modsen.exception;

import com.modsen.constants.DriverServiceConstants;
import lombok.Getter;

@Getter
public class DriverStatusNotFoundException extends RuntimeException {
    public DriverStatusNotFoundException(String driverStatus) {
        super(String.format(DriverServiceConstants.Errors.Message.DRIVERS_WITH_STATUS_NOT_FOUND, driverStatus));
    }
}