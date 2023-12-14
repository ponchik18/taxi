package com.modsen.service.impl;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.balance.DriverBalanceResponse;
import com.modsen.dto.payment.PaymentListResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.dto.payment.PayoutRequest;
import com.modsen.dto.payment.PayoutResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.DriverBalanceNotFound;
import com.modsen.exception.NotEnoughMoneyForPayoutException;
import com.modsen.exception.StripeCustomerNotFoundException;
import com.modsen.mapper.DriverBalanceMapper;
import com.modsen.mapper.PaymentMapper;
import com.modsen.model.DriverBalance;
import com.modsen.model.PageSetting;
import com.modsen.model.Payment;
import com.modsen.model.StripeCustomer;
import com.modsen.repository.DriverBalanceRepository;
import com.modsen.repository.PaymentRepository;
import com.modsen.repository.StripeCustomerRepository;
import com.modsen.service.PaymentService;
import com.modsen.service.StripeService;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.modsen.util.PageRequestFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final PassengerServiceClient passengerServiceClient;
    private final DriverServiceClient driverServiceClient;
    private final StripeCustomerRepository stripeCustomerRepository;

    @Override
    @Transactional
    public PaymentResponse charge(PaymentRequest paymentRequest) {
        validatePassenger(paymentRequest.getPassengerId());
        String customerId = getDefaultCreditCardForPassenger(paymentRequest.getPassengerId());

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

        stripeService.charge(customerId, paymentRequest.getAmount());

        driverBalanceRepository.save(driverBalance);
        return PaymentMapper.MAPPER_INSTANCE.mapToPaymentResponse(
                paymentRepository.save(newPayment)
        );
    }

    private String getDefaultCreditCardForPassenger(Long userId) {
        validatePassenger(userId);
        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUserIdAndUserRole(userId, UserRole.PASSENGER)
                .orElseThrow(() -> new StripeCustomerNotFoundException(userId, UserRole.PASSENGER.name()));

        return stripeCustomer.getCustomerId();
    }

    @Override
    public PaymentListResponse getPaymentHistory(PageSetting pageSetting) {
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        List<Payment> paymentList = paymentRepository.findAll(pageable)
                .toList();
        List<PaymentResponse> paymentResponseList = PaymentMapper.MAPPER_INSTANCE.mapToListOfPaymentResponse(paymentList);
        return PaymentListResponse.builder()
                .payments(paymentResponseList)
                .totalCountOfPayment(paymentResponseList.size())
                .build();
    }

    @Override
    @Transactional
    public PayoutResponse payout(PayoutRequest payoutRequest) {
        validateDriver(payoutRequest.getDriverId());
        DriverBalance driverBalance = driverBalanceRepository.findByDriverId(payoutRequest.getDriverId())
                .orElseThrow(() -> new DriverBalanceNotFound(payoutRequest.getDriverId()));
        BigDecimal actualBalance = driverBalance.getAmount();
        if (actualBalance.compareTo(payoutRequest.getAmount()) < 0) {
            throw new NotEnoughMoneyForPayoutException(actualBalance, payoutRequest.getAmount());
        }

        driverBalance.setAmount(actualBalance.subtract(payoutRequest.getAmount()));
        DriverBalance updatedDriverBalance = driverBalanceRepository.save(driverBalance);
        return PayoutResponse.builder()
                .payoutTime(LocalDateTime.now())
                .actualBalance(updatedDriverBalance.getAmount())
                .driverId(driverBalance.getDriverId())
                .amount(payoutRequest.getAmount())
                .build();
    }

    @Override
    public DriverBalanceResponse getDriverBalance(long driverId) {
        validateDriver(driverId);
        DriverBalance driverBalance = driverBalanceRepository.findByDriverId(driverId)
                .orElseThrow(() -> new DriverBalanceNotFound(driverId));
        return DriverBalanceMapper.MAPPER_INSTANCE.mapToDriverBalanceResponse(driverBalance);
    }

    private void validatePassenger(Long id) {
        passengerServiceClient.getPassengerById(id);
    }

    private void validateDriver(Long id) {
        driverServiceClient.getDriverById(id);
    }
}