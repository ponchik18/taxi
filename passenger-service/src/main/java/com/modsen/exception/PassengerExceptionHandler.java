package com.modsen.exception;

import com.modsen.constants.PassengerServiceConstants;
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
public class PassengerExceptionHandler {

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlePassengerNotFountException(PassengerNotFoundException exception) {

        return ErrorMessage.builder().statusCode(HttpStatus.NOT_FOUND.value()).timestamp(new Date()).message(String.format(PassengerServiceConstants.Errors.Message.USER_NOT_FOUND, exception.getUserId())).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(NoHandlerFoundException exception) {
        return ErrorMessage.builder().statusCode(HttpStatus.NOT_FOUND.value()).timestamp(new Date()).message(exception.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Exception exception) {
        return ErrorMessage.builder().statusCode(HttpStatus.NOT_FOUND.value()).timestamp(new Date()).message(exception.getMessage()).build();
    }
}