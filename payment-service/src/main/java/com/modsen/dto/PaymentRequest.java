package com.modsen.dto;

import com.modsen.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private Long creditCardId;
    private Long passengerId;
    private Long rideId;
    private Long driverId;
}