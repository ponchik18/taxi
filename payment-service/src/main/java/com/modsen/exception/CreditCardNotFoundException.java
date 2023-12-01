package com.modsen.exception;

import lombok.Getter;

@Getter
public class CreditCardNotFoundException extends RuntimeException {
    private final long id;
    public CreditCardNotFoundException(long id) {
        this.id = id;
    }
}