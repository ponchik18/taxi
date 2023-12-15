package com.modsen.exception;

import com.modsen.dto.card.CreditCardRequest;
import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorMessageForCreditCardNotAddResponse(
        int statusCode,
        Date timestamp,
        String message,
        CreditCardRequest card
) {
}