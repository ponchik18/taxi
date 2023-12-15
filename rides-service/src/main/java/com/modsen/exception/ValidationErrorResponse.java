package com.modsen.exception;

import lombok.Builder;

import java.util.Date;
import java.util.Map;

@Builder
public record ValidationErrorResponse(
        Map<String, String> fieldErrors,
        Integer statusCode,
        Date timestamp,
        String message
) {
}