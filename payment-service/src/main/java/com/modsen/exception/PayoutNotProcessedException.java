package com.modsen.exception;

public class PayoutNotProcessedException extends RuntimeException {
    public PayoutNotProcessedException() {
        super("Payout not processed!");
    }
}