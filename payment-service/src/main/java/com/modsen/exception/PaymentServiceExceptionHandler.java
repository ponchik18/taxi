package com.modsen.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class PaymentServiceExceptionHandler {

    @ExceptionHandler(CreditCardNotAddException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageForCreditCardNotAddResponse handleNotFoundException(CreditCardNotAddException exception) {
        return ErrorMessageForCreditCardNotAddResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .card(exception.getCreditCardRequest())
                .build();
    }
}