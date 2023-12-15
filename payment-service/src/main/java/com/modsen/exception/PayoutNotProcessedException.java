package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class PayoutNotProcessedException extends RuntimeException {
    public PayoutNotProcessedException() {
        super(PaymentServiceConstants.Errors.Message.PAYOUT_NOT_PROCESS);
    }
}