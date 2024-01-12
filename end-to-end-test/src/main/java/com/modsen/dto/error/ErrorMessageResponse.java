package com.modsen.dto.error;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorMessageResponse(
        int statusCode,
        Date timestamp,
        String message
) {
}