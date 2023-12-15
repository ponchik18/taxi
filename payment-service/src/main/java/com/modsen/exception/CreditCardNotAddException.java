package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.card.CreditCardRequest;
import lombok.Getter;

@Getter
public class CreditCardNotAddException extends RuntimeException {
    private final CreditCardRequest creditCardRequest;
    public CreditCardNotAddException(CreditCardRequest creditCardRequest) {
        super(String.format(PaymentServiceConstants.Errors.Message.CREDIT_CARD_NOT_ADD, creditCardRequest.getCardNumber()));
        creditCardRequest.setCvc("***");
        this.creditCardRequest = creditCardRequest;
    }
}