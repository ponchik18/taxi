package com.modsen.exception;

import lombok.Getter;

@Getter
public class DriverBalanceNotFound extends RuntimeException {
    public DriverBalanceNotFound(Long driverId) {
        super(String.format("Driver balance with id = '%d' not found!", driverId));
    }
}