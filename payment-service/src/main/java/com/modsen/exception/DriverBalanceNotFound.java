package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;
import lombok.Getter;

@Getter
public class DriverBalanceNotFound extends RuntimeException {
    public DriverBalanceNotFound(Long driverId) {
        super(String.format(PaymentServiceConstants.Errors.Message.DRIVER_BALANCE_NOT_FOUNT, driverId));
    }
}