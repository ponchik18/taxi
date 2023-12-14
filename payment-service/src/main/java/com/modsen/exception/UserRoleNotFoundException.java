package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String userRole) {
        super(String.format(PaymentServiceConstants.Errors.Message.USER_ROLE_NOT_FOUND, userRole));
    }
}