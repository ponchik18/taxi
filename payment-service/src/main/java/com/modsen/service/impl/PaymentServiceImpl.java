package com.modsen.service.impl;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.PaymentListResponse;
import com.modsen.dto.PaymentRequest;
import com.modsen.dto.PaymentResponse;
import com.modsen.dto.PayoutRequest;
import com.modsen.dto.PayoutResponse;
import com.modsen.enums.PaymentMethod;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotFoundException;
import com.modsen.exception.DriverBalanceNotFound;
import com.modsen.exception.NotRightAmountForPayout;
import com.modsen.mapper.PaymentMapper;
import com.modsen.model.CreditCard;
import com.modsen.model.DriverBalance;
import com.modsen.model.Payment;
import com.modsen.repository.CreditCardRepository;
import com.modsen.repository.DriverBalanceRepository;
import com.modsen.repository.PaymentRepository;
import com.modsen.service.PaymentService;
import com.modsen.service.StripeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final DriverBalanceRepository driverBalanceRepository;
    private final StripeService stripeService;
    private final CreditCardRepository creditCardRepository;

    @Override
    @Transactional
    public PaymentResponse charge(PaymentRequest paymentRequest) {
        Payment newPayment = PaymentMapper.MAPPER_INSTANCE.mapToPayment(paymentRequest);
        newPayment.setPaymentDate(LocalDateTime.now());


        DriverBalance driverBalance = driverBalanceRepository.findByDriverId(paymentRequest.getDriverId())
                .orElse(
                    DriverBalance.builder()
                            .driverId(paymentRequest.getDriverId())
                            .amount(BigDecimal.ZERO)
                            .build()
                );
        BigDecimal driverAmount = paymentRequest.getAmount()
                .multiply(PaymentServiceConstants.DefaultValue.DRIVE_PERCENTAGE_OF_THE_RIDE)
                .setScale(2, RoundingMode.HALF_UP);
        driverBalance.setAmount(
                driverBalance.getAmount()
                        .add(driverAmount)
        );

        if(newPayment.getPaymentMethod() == PaymentMethod.CARD) {
            CreditCard creditCard = creditCardRepository.findById(paymentRequest.getCreditCardId())
                    .orElseThrow(()->new CreditCardNotFoundException(paymentRequest.getCreditCardId()));
            stripeService.charge(creditCard.getToken(), paymentRequest.getAmount());
        }
        driverBalanceRepository.save(driverBalance);
        return PaymentMapper.MAPPER_INSTANCE.mapToPaymentResponse(
                paymentRepository.save(newPayment)
        );
    }

    @Override
    public PaymentListResponse getPaymentHistory(long passengerId) {
        List<Payment> paymentList = paymentRepository.findAllByRideId(passengerId);
        List<PaymentResponse> paymentResponseList= PaymentMapper.MAPPER_INSTANCE.mapToListOfPaymentResponse(paymentList);
        return PaymentListResponse.builder()
                .payments(paymentResponseList)
                .totalCountOfPayment(paymentResponseList.size())
                .build();
    }

    @Override
    @Transactional
    public PayoutResponse payout(PayoutRequest payoutRequest) {
        CreditCard creditCard = creditCardRepository.findById(payoutRequest.getCardId())
                .orElseThrow(()->new CreditCardNotFoundException(payoutRequest.getCardId()));
        DriverBalance driverBalance = driverBalanceRepository.findByDriverId(payoutRequest.getDriverId())
                .orElseThrow(()->new DriverBalanceNotFound(payoutRequest.getDriverId()));
        BigDecimal actualBalance = driverBalance.getAmount();
        if(actualBalance.compareTo(payoutRequest.getAmount())<=0){
            throw new NotRightAmountForPayout(actualBalance);
        }

        driverBalance.setAmount(actualBalance.subtract(payoutRequest.getAmount()));
        DriverBalance updatedDriverBalance = driverBalanceRepository.save(driverBalance);
        stripeService.processPayout(creditCard.getToken(), payoutRequest.getAmount());
        return PayoutResponse.builder()
                .payoutTime(LocalDateTime.now())
                .actualBalance(updatedDriverBalance.getAmount())
                .driverId(driverBalance.getDriverId())
                .amount(payoutRequest.getAmount())
                .build();
    }
}