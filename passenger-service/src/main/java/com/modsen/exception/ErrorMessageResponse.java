package com.modsen.exception;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorMessageResponse(
        int statusCode,
        Date timestamp,
        String message
) {
}