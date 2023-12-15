package com.modsen.exception;

import com.modsen.constants.RatingServiceConstants;
import lombok.Getter;

@Getter
public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String userRole) {
        super(String.format(RatingServiceConstants.Errors.Message.USER_ROLE_NOT_FOUND, userRole));
    }
}