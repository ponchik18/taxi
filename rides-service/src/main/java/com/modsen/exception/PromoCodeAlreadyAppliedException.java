package com.modsen.exception;

import com.modsen.constants.RidesServiceConstants;

public class PromoCodeAlreadyAppliedException extends RuntimeException {
    public PromoCodeAlreadyAppliedException(String promoCode) {
        super(String.format(RidesServiceConstants.Errors.Message.PROMO_CODE_ALREADY_APPLIED, promoCode));
    }
}