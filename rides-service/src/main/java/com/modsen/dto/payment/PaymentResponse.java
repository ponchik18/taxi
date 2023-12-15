package com.modsen.dto.payment;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        Long id,
        BigDecimal amount,
        LocalDateTime paymentDate,
        Long rideId
) {
}