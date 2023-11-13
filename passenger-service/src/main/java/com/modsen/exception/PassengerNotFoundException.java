package com.modsen.exception;

import com.modsen.constants.PassengerServiceConstants;
import lombok.Getter;

@Getter
public class PassengerNotFoundException extends RuntimeException {
    private final long userId;
    public PassengerNotFoundException(long id) {
        super(String.format(PassengerServiceConstants.Errors.Message.USER_NOT_FOUND, id));
        this.userId = id;
    }
}