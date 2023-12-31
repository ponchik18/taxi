package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(long id) {
        super(String.format(PaymentServiceConstants.Errors.Message.CREDIT_CARD_NOT_ADD, id));
    }
}