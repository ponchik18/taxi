package com.modsen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayoutRequest {
    private Long driverId;
    private Long cardId;
    private BigDecimal amount;
}