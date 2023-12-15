package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class RideNotPaidException extends RuntimeException {
    public RideNotPaidException() {
        super(PaymentServiceConstants.Errors.Message.RIDE_NOT_PAID);
    }
}