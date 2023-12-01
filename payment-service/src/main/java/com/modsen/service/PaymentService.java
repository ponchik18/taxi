package com.modsen.service;

import com.modsen.dto.PaymentRequest;
import com.modsen.dto.PaymentResponse;
import com.modsen.dto.PaymentListResponse;
import com.modsen.dto.PayoutRequest;
import com.modsen.dto.PayoutResponse;

public interface PaymentService {
    PaymentResponse charge(PaymentRequest paymentRequest);
    PaymentListResponse getPaymentHistory(long passengerId);

    PayoutResponse payout(PayoutRequest payoutRequest);
}