package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler({CreditCardNotFoundException.class,
            DriverBalanceNotFound.class, UserNotFoundException.class,
            DefaultCreditCardNotFoundException.class, UserRoleNotFoundException.class, NoHandlerFoundException.class,
            EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleNotFoundException(Exception exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler({NotRightAmountForPayout.class, PayoutNotProcessedException.class,  })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponse handleServerErrorException(Exception exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ValidationErrorResponse.builder()
                .fieldErrors(errors)
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(PaymentServiceConstants.Errors.Message.NOT_VALID_FIELD)
                .build();
    }
}