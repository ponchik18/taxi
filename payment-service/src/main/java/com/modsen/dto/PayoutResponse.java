package com.modsen.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PayoutResponse(
        Long driverId,
        BigDecimal amount,
        LocalDateTime payoutTime,
        BigDecimal actualBalance
) {
}