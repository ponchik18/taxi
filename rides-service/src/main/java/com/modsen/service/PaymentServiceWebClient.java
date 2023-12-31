package com.modsen.service;


import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.payment.PaymentRequest;

import java.util.Optional;

public interface PaymentServiceWebClient {
    void makeCharge(PaymentRequest paymentRequest);
    Optional<CreditCardResponse> getDefaultCardForPassenger(long passengerId);
}