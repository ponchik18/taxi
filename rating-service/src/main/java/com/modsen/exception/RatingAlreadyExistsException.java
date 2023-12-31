package com.modsen.exception;

import com.modsen.constants.RatingServiceConstants;

public class RatingAlreadyExistsException extends RuntimeException {
    public RatingAlreadyExistsException(Long rentId, String userRole) {
        super(String.format(RatingServiceConstants.Errors.Message.RATING_ALREADY_CREATED, userRole, rentId));
    }
}