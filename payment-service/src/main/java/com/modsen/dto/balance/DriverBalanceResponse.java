package com.modsen.dto.balance;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DriverBalanceResponse(
        Long id,
        Long driverId,
        BigDecimal amount
) {
}