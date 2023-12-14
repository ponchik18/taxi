package com.modsen.service.impl;

import com.modsen.dto.card.CreditCardRequest;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.RideNotPaidException;
import com.modsen.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import com.stripe.param.CustomerRetrieveParams;
import com.stripe.param.CustomerUpdateParams;
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
        try {
            return Customer.retrieve(customerId);
        } catch (Exception e) {
            return createCustomer(email);
        }
    }

    @Override
    public String createCardForCustomer(String customerId, Token token) throws StripeException {
        Customer customer = getCustomer(customerId);
        String stripeCardId = customer.getSources()
                .create(Map.of("card", token.getId()))
                .getId();
        updateDefaultCard(stripeCardId, customer);
        return stripeCardId;
    }

    @Override
    public String getDefaultCardIdForCustomer(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        return customer.getDefaultSource();
    }

    @Override
    public void makeCreditCardDefault(String customerId, String stripeCardId) throws StripeException {
        Customer customer = getCustomer(customerId);
        updateDefaultCard(stripeCardId, customer);
    }

    @Override
    public void deleteCard(String customerId, String cardId) throws StripeException {
        Customer customer = getCustomer(customerId);
        Card card = (Card) customer.getSources()
                .retrieve(cardId);
        card.delete();
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

    private Customer getCustomer(String customerId) throws StripeException {
        CustomerRetrieveParams params = CustomerRetrieveParams.builder()
                .addExpand("sources")
                .build();
        return Customer.retrieve(customerId, params, null);
    }

    private void updateDefaultCard(String cardId, Customer customer) throws StripeException {
        CustomerUpdateParams customerUpdateParams = new CustomerUpdateParams.Builder()
                .setInvoiceSettings(
                        CustomerUpdateParams.InvoiceSettings.builder()
                                .setDefaultPaymentMethod(cardId)
                                .build()
                )
                .build();
        customer.update(customerUpdateParams);
    }
}