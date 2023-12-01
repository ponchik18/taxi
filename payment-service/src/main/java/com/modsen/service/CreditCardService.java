package com.modsen.service;

import com.modsen.dto.CreditCardListResponse;
import com.modsen.dto.CreditCardRequest;
import com.modsen.dto.CreditCardResponse;
import com.modsen.enums.UserRole;

public interface CreditCardService {
    CreditCardResponse addCreditCard(CreditCardRequest creditCardRequest);
    void deleteCreditCard(long id);
    CreditCardResponse getCreditCardById(long id);
    CreditCardListResponse getAllUserCreditCard(long userId, UserRole userRole);
}