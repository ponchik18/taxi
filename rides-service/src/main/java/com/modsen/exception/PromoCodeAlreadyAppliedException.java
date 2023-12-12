package com.modsen.exception;

public class PromoCodeAlreadyAppliedException extends RuntimeException {
    public PromoCodeAlreadyAppliedException(String promoCode) {
        super(String.format("Promo code '%s' has been already applied!", promoCode));
    }
}