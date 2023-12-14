package com.modsen.exception;

import com.modsen.constants.PaymentServiceConstants;

import java.math.BigDecimal;


public class NotEnoughMoneyForPayoutException extends RuntimeException {

    public NotEnoughMoneyForPayoutException(BigDecimal actualBalance, BigDecimal amount) {
        super(String.format(PaymentServiceConstants.Errors.Message.NOT_ENOUGH_MONEY,
                actualBalance.floatValue(), amount.floatValue()));
    }
}