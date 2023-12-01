package com.modsen.exception;

import lombok.Getter;

@Getter
public class DriverBalanceNotFound extends RuntimeException {
    private final Long driverId;
    public DriverBalanceNotFound(Long driverId) {
        this.driverId = driverId;
    }
}