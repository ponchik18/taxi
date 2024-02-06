package com.modsen.contoller;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.balance.DriverBalanceResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.dto.payment.PaymentListResponse;
import com.modsen.dto.payment.PayoutRequest;
import com.modsen.dto.payment.PayoutResponse;
import com.modsen.model.PageSetting;
import com.modsen.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PaymentServiceConstants.BasePath.PAYMENT_CONTROLLER_PATH)
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentListResponse getPaymentHistory(PageSetting pageSetting) {
        return paymentService.getPaymentHistory(pageSetting);
    }

    @PostMapping("/charge")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PASSENGER') || hasRole('DRIVER')")
    public PaymentResponse charge(@Valid  @RequestBody PaymentRequest paymentRequest) {
        return paymentService.charge(paymentRequest);
    }

    @PostMapping("/payout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('DRIVER')")
    public PayoutResponse payout(@Valid @RequestBody PayoutRequest payoutRequest) {
        return paymentService.payout(payoutRequest);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DRIVER')")
    public DriverBalanceResponse getDriverBalance(@RequestParam long driverId) {
        return paymentService.getDriverBalance(driverId);
    }
}