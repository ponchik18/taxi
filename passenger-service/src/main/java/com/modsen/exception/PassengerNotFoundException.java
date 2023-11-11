package com.modsen.exception;

import lombok.Getter;

@Getter
public class PassengerNotFoundException extends RuntimeException {
    private final Integer userId;
    public PassengerNotFoundException(Integer id) {
        this.userId = id;
    }
}