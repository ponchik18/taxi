package com.modsen.exception;

public class DefaultCreditCardNotFoundException extends RuntimeException {
    public DefaultCreditCardNotFoundException(String userRole, long userId) {
        super(String.format("User with id '%d' and role '%s' hasn't default card!", userId, userRole));
    }
}