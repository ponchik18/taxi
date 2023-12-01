package com.modsen.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentListResponse(
        List<PaymentResponse> payments,
        int totalCountOfPayment
) {
}