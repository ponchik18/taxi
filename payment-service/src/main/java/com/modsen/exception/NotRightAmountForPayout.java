package com.modsen.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class NotRightAmountForPayout extends RuntimeException {
    private final BigDecimal actualBalance;
    public NotRightAmountForPayout(BigDecimal actualBalance) {
        this.actualBalance = actualBalance;
    }
}