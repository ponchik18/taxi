package com.modsen.dto.card;

import com.modsen.enums.UserRole;
import lombok.Builder;

@Builder
public record CreditCardResponse(
        Long id,
        String cardNumber,
        String cardHolder,
        UserRole userRole,
        Long userId,
        String stripeCardId
) {
}