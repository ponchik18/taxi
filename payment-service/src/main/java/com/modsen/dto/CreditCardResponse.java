package com.modsen.dto;

import com.modsen.enums.UserRole;
import lombok.Builder;

@Builder
public record CreditCardResponse(
        Long id,
        String cardNumber,
        String cardHolder,
        String token,
        UserRole userRole,
        Long userId
) {
}