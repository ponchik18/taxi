package com.modsen.exception;

import com.modsen.constants.RatingServiceConstants;
import lombok.Getter;

@Getter
public class RatingNotFoundException extends RuntimeException {
    private final long id;

    public RatingNotFoundException(long id) {
        super(String.format(RatingServiceConstants.Errors.Message.RATING_NOT_FOUND, id));
        this.id = id;
    }
}