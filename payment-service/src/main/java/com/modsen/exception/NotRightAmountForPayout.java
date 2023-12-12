package com.modsen.exception;

import java.math.BigDecimal;


public class NotRightAmountForPayout extends RuntimeException {

    public NotRightAmountForPayout(BigDecimal actualBalance, BigDecimal amount) {
        super(String.format("Not right amount for payout! Actual balance is '%f'. But you enter '%f'",
                actualBalance.floatValue(), amount.floatValue()));
    }
}