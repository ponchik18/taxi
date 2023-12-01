package com.modsen.contoller;

import com.modsen.dto.PaymentRequest;
import com.modsen.dto.PaymentResponse;
import com.modsen.dto.PaymentListResponse;
import com.modsen.dto.PayoutRequest;
import com.modsen.dto.PayoutResponse;
import com.modsen.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public PaymentListResponse getPaymentHistory(@RequestParam long passengerId) {
        return paymentService.getPaymentHistory(passengerId);
    }

    @PostMapping("/charge")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse charge(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.charge(paymentRequest);
    }

    @PostMapping("/payout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PayoutResponse payout(@RequestBody PayoutRequest payoutRequest) {
        return paymentService.payout(payoutRequest);
    }
}