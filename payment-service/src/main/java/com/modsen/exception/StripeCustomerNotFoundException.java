package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class StripeCustomerNotFoundException extends RuntimeException {
    public StripeCustomerNotFoundException(long userId, String userRole) {
        super(String.format(PaymentServiceConstants.Errors.Message.STRIPE_CUSTOMER_NOT_FOUND, userId, userRole));
    }
}