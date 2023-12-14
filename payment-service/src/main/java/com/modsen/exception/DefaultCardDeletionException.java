package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

public class DefaultCardDeletionException extends RuntimeException {
    public DefaultCardDeletionException(long cardId) {
        super(String.format(PaymentServiceConstants.Errors.Message.DEFAULT_CREDIT_CARD_DELETION, cardId));
    }
}