package com.modsen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.exception.ErrorMessageResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorMessageResponse message;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ErrorMessageResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        if (responseStatus.is4xxClientError()) {
            return new EntityNotFoundException(message.getMessage());
        } else {
            return new Exception(message.getMessage());
        }

    }
}