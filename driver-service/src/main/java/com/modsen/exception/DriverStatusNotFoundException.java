package com.modsen.exception;

import lombok.Getter;

@Getter
public class DriverStatusNotFoundException extends RuntimeException {

    public DriverStatusNotFoundException(String driverStatus) {
        super("Driver status '"+driverStatus+"' not found!");
    }
}