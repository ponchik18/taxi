package com.modsen.dto.card;

import com.modsen.enums.UserRole;
import lombok.Builder;

@Builder
public record CreditCardResponse(
        Long id,
        String cardNumber,
        String cardHolder,
        String customerId,
        UserRole userRole,
        Long userId,
        String token
) {
}