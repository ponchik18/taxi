package com.modsen.exception;

import com.modsen.constants.RatingServiceConstants;

public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException(long id) {
        super(String.format(RatingServiceConstants.Errors.Message.RATING_NOT_FOUND, id));
    }
}