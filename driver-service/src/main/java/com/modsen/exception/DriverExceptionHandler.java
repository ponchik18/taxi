package com.modsen.exception;

import com.modsen.constants.DriverServiceConstants;
import org.springframework.dao.DuplicateKeyException;
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
public class DriverExceptionHandler {
    @ExceptionHandler({DriverNotFoundException.class, NoHandlerFoundException.class, DriverStatusNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleNotFoundException(DriverNotFoundException exception) {

        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
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
                .message(DriverServiceConstants.Errors.Message.NOT_VALID_FIELD)
                .build();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageResponse handleDuplicateKeyException(DuplicateKeyException exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponse handleInternalServerError(Exception exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }
}