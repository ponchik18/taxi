package com.modsen.service;

import com.modsen.dto.balance.DriverBalanceResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.dto.payment.PaymentListResponse;
import com.modsen.dto.payment.PayoutRequest;
import com.modsen.dto.payment.PayoutResponse;
import com.modsen.model.PageSetting;

public interface PaymentService {
    PaymentResponse charge(PaymentRequest paymentRequest);
    PaymentListResponse getPaymentHistory(PageSetting pageSetting);

    PayoutResponse payout(PayoutRequest payoutRequest);

    DriverBalanceResponse getDriverBalance(long driverId);
}