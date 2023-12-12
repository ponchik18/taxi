package com.modsen.service;

import com.modsen.dto.card.CreditCardListResponse;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.card.DefaultCreditCardRequest;

public interface CreditCardService {
    CreditCardResponse addCreditCard(CreditCardRequest creditCardRequest);
    void deleteCreditCard(long id);
    CreditCardResponse getCreditCardById(long id);
    CreditCardListResponse getAllUserCreditCard(long userId, String userRole);

    CreditCardResponse makeCreditCardDefault(DefaultCreditCardRequest defaultCreditCardRequest);

    CreditCardResponse getDefaultCardForUser(long userId, String userRole);
}