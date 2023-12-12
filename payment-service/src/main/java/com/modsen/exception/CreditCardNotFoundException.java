package com.modsen.exception;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(long id) {
        super(String.format("Card with id = '%d' not found!", id));
    }
}