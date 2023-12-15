package com.modsen.exception;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.enums.UserRole;

public class RatingAlreadyExistsException extends RuntimeException {
    public RatingAlreadyExistsException(Long rentId, UserRole userRole) {
        super(String.format(RatingServiceConstants.Errors.Message.RATING_ALREADY_CREATED, userRole, rentId));
    }
}