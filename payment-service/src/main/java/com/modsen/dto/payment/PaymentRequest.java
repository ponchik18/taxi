package com.modsen.dto.payment;

import com.modsen.constants.PaymentServiceConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private BigDecimal amount;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long passengerId;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long rideId;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long driverId;
}