package com.modsen.service;

import com.modsen.dto.card.CreditCardRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import java.math.BigDecimal;

public interface StripeService {
    Token createCardToken(CreditCardRequest creditCardRequest);

    void charge(String token, BigDecimal amount);

    Customer getCustomer(String customerId, String email);

    String createCardForCustomer(String customer, Token token) throws StripeException;
    String getDefaultCardIdForCustomer(String customerId) throws StripeException;

    void makeCreditCardDefault(String customerId, String stripeCardId) throws StripeException;

    void deleteCard(String customerId, String cardId) throws StripeException;
}