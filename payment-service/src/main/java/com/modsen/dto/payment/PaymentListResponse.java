package com.modsen.dto.payment;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentListResponse(
        List<PaymentResponse> payments,
        int countOfPayment
) {
}