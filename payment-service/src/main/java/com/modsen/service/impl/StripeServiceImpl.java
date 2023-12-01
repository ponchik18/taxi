package com.modsen.service.impl;

import com.modsen.dto.CreditCardRequest;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.PayoutNotProcessedException;
import com.modsen.exception.RideNotPaidException;
import com.modsen.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Payout;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    public Charge charge(String token, BigDecimal amount) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount.multiply(
                    BigDecimal.valueOf(100)
            ).intValue());
            params.put("currency", "USD");
            params.put("source", token);
            return Charge.create(params);
        } catch (StripeException exception) {
            throw new RideNotPaidException();
        }
    }

    @Override
    public void processPayout(String recipientToken, BigDecimal amount) {
        try{
            Map<String, Object> payoutParams  = new HashMap<>();
            payoutParams .put("amount", amount.multiply(
                    BigDecimal.valueOf(100)
            ).intValue());
            payoutParams .put("currency", "USD");
            payoutParams .put("destination", recipientToken);
            Payout.create(payoutParams);
        } catch (StripeException exception) {
            throw new PayoutNotProcessedException();
        }
    }
}