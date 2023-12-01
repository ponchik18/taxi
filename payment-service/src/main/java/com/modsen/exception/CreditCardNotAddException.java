package com.modsen.exception;

import com.modsen.dto.CreditCardRequest;
import lombok.Getter;

@Getter
public class CreditCardNotAddException extends RuntimeException {
    private final CreditCardRequest creditCardRequest;
    public CreditCardNotAddException(CreditCardRequest creditCardRequest) {
        super(String.format("Card with number '%s' not add!", creditCardRequest.getCardNumber()));
        creditCardRequest.setCvc("***");
        this.creditCardRequest = creditCardRequest;
    }
}