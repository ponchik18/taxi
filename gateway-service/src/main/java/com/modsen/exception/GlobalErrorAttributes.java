package com.modsen.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest serverRequest, ErrorAttributeOptions options) {
        Throwable error = this.getError(serverRequest);
        Map<String, Object> map = super.getErrorAttributes(serverRequest, options);
        map.remove("trace");
        map.remove("requestId");
        map.put("message", error.getMessage());
        return map;
    }
}