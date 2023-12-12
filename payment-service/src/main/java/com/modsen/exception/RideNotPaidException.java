package com.modsen.exception;

public class RideNotPaidException extends RuntimeException {
    public RideNotPaidException() {
        super("Error! Ride not paid!");
    }
}