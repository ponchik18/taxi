package com.modsen.service.impl;

import com.modsen.dto.card.CreditCardRequest;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.RideNotPaidException;
import com.modsen.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Token createCardToken(CreditCardRequest creditCardRequest) {
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", creditCardRequest.getCardNumber());
            card.put("exp_month", Integer.parseInt(creditCardRequest.getExpMonth()));
            card.put("exp_year", Integer.parseInt(creditCardRequest.getExpYear()));
            card.put("cvc", creditCardRequest.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);
            return Token.create(params);
        } catch (StripeException exception) {
            throw new CreditCardNotAddException(creditCardRequest);
        }
    }

    @Override
    public void charge(String token, BigDecimal amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount.multiply(
                    BigDecimal.valueOf(100)
            ).intValue());
            params.put("currency", "USD");
            params.put("customer", token);
            Charge.create(params);
        } catch (StripeException exception) {
            throw new RideNotPaidException();
        }
    }

    @Override
    public Customer getCustomer(String customerId, String email) {
        Customer customer = getCustomer(customerId);
        if (Objects.isNull(customer)) {
            customer = createCustomer(email);
        }
        return customer;
    }

    @Override
    public void addSourceToCustomer(Customer customer, Token token) throws StripeException {
        customer.update(Map.of("source", token.getId()));
    }

    @Override
    public Token getTokenById(String id) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException, CardException {
        return Token.retrieve(id);
    }

    private Customer createCustomer(String email) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);
            return Customer.create(params);
        } catch (StripeException e) {
            return null;
        }
    }

    private Customer getCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (Exception e) {
            return null;
        }
    }


}