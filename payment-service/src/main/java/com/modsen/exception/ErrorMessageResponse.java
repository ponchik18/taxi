package com.modsen.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ErrorMessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
}