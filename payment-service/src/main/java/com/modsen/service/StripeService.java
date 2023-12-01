package com.modsen.service;

import com.modsen.dto.CreditCardRequest;
import com.modsen.dto.PaymentRequest;
import com.modsen.model.CreditCard;
import com.modsen.model.Payment;
import com.stripe.model.Charge;
import com.stripe.model.Token;

import java.math.BigDecimal;

public interface StripeService {
    Token createCardToken(CreditCardRequest creditCardRequest);
    Charge charge(String token, BigDecimal amount);

    void processPayout(String recipientToken, BigDecimal amount);
}