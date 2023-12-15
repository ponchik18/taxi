package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class DefaultCreditCardNotFoundException extends RuntimeException {
    public DefaultCreditCardNotFoundException(String userRole, long userId) {
        super(String.format(PaymentServiceConstants.Errors.Message.DEFAULT_CREDIT_CARD_NOT_FOUND, userId, userRole));
    }
}