package com.modsen.dto.payment;

import com.modsen.constants.PaymentServiceConstants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayoutRequest {
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long driverId;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long cardId;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    @DecimalMin(value = PaymentServiceConstants.DefaultValue.MIN_AMOUNT, inclusive = false, message = PaymentServiceConstants.Validation.Message.MORE_THAN_ZERO)
    private BigDecimal amount;
}