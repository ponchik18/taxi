package com.modsen.service.impl;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.dto.balance.DriverBalanceResponse;
import com.modsen.dto.payment.PaymentListResponse;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PayoutRequest;
import com.modsen.dto.payment.PayoutResponse;
import com.modsen.dto.users.DriverResponse;
import com.modsen.dto.users.PassengerResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.DriverBalanceNotFound;
import com.modsen.exception.NotEnoughMoneyForPayoutException;
import com.modsen.model.DriverBalance;
import com.modsen.model.PageSetting;
import com.modsen.model.Payment;
import com.modsen.model.StripeCustomer;
import com.modsen.repository.DriverBalanceRepository;
import com.modsen.repository.PaymentRepository;
import com.modsen.repository.StripeCustomerRepository;
import com.modsen.service.StripeService;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.modsen.util.PageRequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private DriverBalanceRepository driverBalanceRepository;
    @Mock
    private StripeService stripeService;
    @Mock
    private PassengerServiceClient passengerServiceClient;
    @Mock
    private DriverServiceClient driverServiceClient;
    @Mock
    private StripeCustomerRepository stripeCustomerRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;
    private PaymentRequest paymentRequest;
    private StripeCustomer stripeCustomer;
    private DriverBalance driverBalance;
    private Payment payment;
    private PayoutRequest payoutRequest;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest(BigDecimal.TEN, PaymentServiceTestConstants.TestData.PASSENGER_ID, PaymentServiceTestConstants.TestData.RIDE_ID, PaymentServiceTestConstants.TestData.DRIVER_ID);
        stripeCustomer = StripeCustomer.builder()
                .customerId(PaymentServiceTestConstants.TestData.CUSTOMER_ID)
                .userId(PaymentServiceTestConstants.TestData.PASSENGER_ID)
                .userRole(UserRole.PASSENGER)
                .build();
        driverBalance = DriverBalance.builder()
                .driverId(PaymentServiceTestConstants.TestData.DRIVER_ID)
                .amount(BigDecimal.TEN)
                .build();
        payment = Payment.builder()
                .amount(BigDecimal.TEN)
                .rideId(PaymentServiceTestConstants.TestData.RIDE_ID)
                .build();
        payoutRequest = new PayoutRequest(PaymentServiceTestConstants.TestData.DRIVER_ID, PaymentServiceTestConstants.TestData.CARD_ID, BigDecimal.ONE);
    }

    @Test
    void charge_ValidPaymentRequest_Success() {
        DriverBalance expectedDriverBalance = DriverBalance.builder()
                .driverId(paymentRequest.getDriverId())
                .amount(new BigDecimal("2.00"))
                .build();
        Payment expectedPayment = payment;

        when(passengerServiceClient.getPassengerById(anyLong()))
                .thenReturn(PassengerResponse.builder().build());
        when(stripeCustomerRepository.findByUserIdAndUserRole(anyLong(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(driverBalanceRepository.findByDriverId(payoutRequest.getDriverId()))
                .thenReturn(Optional.empty());
        paymentService.charge(paymentRequest);

        ArgumentCaptor<DriverBalance> driverBalanceArgumentCaptor = ArgumentCaptor.forClass(DriverBalance.class);
        verify(driverBalanceRepository, times(1))
                .save(driverBalanceArgumentCaptor.capture());
        assertThat(driverBalanceArgumentCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDriverBalance);

        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1))
                .save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id", "paymentDate")
                .isEqualTo(expectedPayment);

        ArgumentCaptor<BigDecimal> amountArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        verify(stripeService, times(1))
                .charge(any(), amountArgumentCaptor.capture());
        assertThat(amountArgumentCaptor.getValue())
                .isEqualTo(paymentRequest.getAmount());
    }

    @Test
    void getPaymentHistory_ValidPageSetting_Success() {
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);

        List<Payment> expectedPassengers = Collections.singletonList(payment);
        Page<Payment> page = new PageImpl<>(expectedPassengers);

        when(paymentRepository.findAll(pageable))
                .thenReturn(page);

        PaymentListResponse actualPaymentListResponse = paymentService.getPaymentHistory(pageSetting);

        assertNotNull(actualPaymentListResponse);
        assertThat(expectedPassengers.size())
                .isEqualTo(actualPaymentListResponse.countOfPayment());
        assertThat(expectedPassengers.size())
                .isEqualTo(actualPaymentListResponse.payments().size());
    }

    @Test
    void payout_ValidPayoutRequest_Success() {
        BigDecimal expectedValue = BigDecimal.valueOf(9L);

        when(driverBalanceRepository.findByDriverId(anyLong()))
                .thenReturn(Optional.of(driverBalance));
        when(driverBalanceRepository.save(any(DriverBalance.class)))
                .thenReturn(driverBalance);

        PayoutResponse actualPayoutResponse = paymentService.payout(payoutRequest);

        assertNotNull(actualPayoutResponse);
        ArgumentCaptor<DriverBalance> driverBalanceArgumentCaptor = ArgumentCaptor.forClass(DriverBalance.class);
        verify(driverBalanceRepository, times(1))
                .save(driverBalanceArgumentCaptor.capture());
        DriverBalance actualDriverBalance = driverBalanceArgumentCaptor.getValue();
        assertThat(actualDriverBalance.getAmount())
                .isEqualTo(expectedValue);
    }

    @Test
    void payout_DriverBalanceNotExist_ThrowException() {
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.DRIVER_BALANCE_NOT_FOUNT, paymentRequest.getDriverId());

        when(driverBalanceRepository.findByDriverId(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.payout(payoutRequest))
                .isInstanceOf(DriverBalanceNotFound.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    void payout_NotEnoughMoney_ThrowException() {
        driverBalance.setAmount(BigDecimal.ZERO);
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.NOT_ENOUGH_MONEY, driverBalance.getAmount(), payoutRequest.getAmount());

        when(driverBalanceRepository.findByDriverId(anyLong()))
                .thenReturn(Optional.of(driverBalance));

        assertThatThrownBy(() -> paymentService.payout(payoutRequest))
                .isInstanceOf(NotEnoughMoneyForPayoutException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    void getDriverBalance_ExistingId_Success() {
        long driverId = PaymentServiceTestConstants.TestData.DRIVER_ID;

        when(driverServiceClient.getDriverById(driverId))
                .thenReturn(DriverResponse.builder().build());
        when(driverBalanceRepository.findByDriverId(driverId))
                .thenReturn(Optional.of(driverBalance));

        DriverBalanceResponse actualDriverBalanceResponse = paymentService.getDriverBalance(driverId);

        verify(driverBalanceRepository, times(1))
                .findByDriverId(driverId);
        assertThat(actualDriverBalanceResponse).usingRecursiveComparison()
                .isEqualTo(driverBalance);
    }

    @Test
    void getDriverBalance_UnExistingId_Success() {
        long driverId = 999L;
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.DRIVER_BALANCE_NOT_FOUNT, driverId);

        when(driverServiceClient.getDriverById(anyLong()))
                .thenReturn(DriverResponse.builder().build());
        when(driverBalanceRepository.findByDriverId(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getDriverBalance(driverId))
                .isInstanceOf(DriverBalanceNotFound.class)
                .hasMessageContaining(expectedMessage);
    }
}