package com.modsen.exception;

public class PromoCodeNotFoundException extends RuntimeException {
    public PromoCodeNotFoundException(String promoCode) {
        super(String.format("Promo-code with name '%s' not found!", promoCode));
    }
}