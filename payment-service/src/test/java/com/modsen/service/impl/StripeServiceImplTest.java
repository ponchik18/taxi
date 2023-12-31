package com.modsen.service.impl;

import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.RideNotPaidException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;

@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @InjectMocks
    private StripeServiceImpl stripeServiceImpl;
    private CreditCardRequest creditCardRequest;

    @BeforeEach
    public void setUp() {
        creditCardRequest = new CreditCardRequest();
        creditCardRequest.setCardHolder(PaymentServiceTestConstants.TestData.CARD_HOLDER);
        creditCardRequest.setCardNumber(PaymentServiceTestConstants.TestData.CARD_NUMBER);
        creditCardRequest.setExpMonth(PaymentServiceTestConstants.TestData.EXP_MONTH);
        creditCardRequest.setExpYear(PaymentServiceTestConstants.TestData.EXP_YEAR);
        creditCardRequest.setCvc(PaymentServiceTestConstants.TestData.CVC);
        creditCardRequest.setUserRole(UserRole.PASSENGER.name());
        creditCardRequest.setUserId(PaymentServiceTestConstants.TestData.PASSENGER_ID);
    }

    @Test
    public void createCardToken_ValidCreditCardRequest_Success() {
        Token expectedToken = new Token();
        expectedToken.setId("tok_123456789");

        Token actualToken;
        try (MockedStatic<Token> tokenMockedStatic = Mockito.mockStatic(Token.class)) {
            tokenMockedStatic.when(() -> Token.create(anyMap()))
                    .thenReturn(expectedToken);
            actualToken = stripeServiceImpl.createCardToken(creditCardRequest);
        }

        assertThat(actualToken.getId())
                .isEqualTo(expectedToken.getId());
    }

    @Test
    public void createCardToken_InValidCreditCardRequest_ThrowException() {
        try (MockedStatic<Token> tokenMockedStatic = Mockito.mockStatic(Token.class)) {
            tokenMockedStatic.when(() -> Token.create(anyMap()))
                    .thenThrow(CreditCardNotAddException.class);
            assertThatThrownBy(() -> stripeServiceImpl.createCardToken(creditCardRequest))
                    .isInstanceOf(CreditCardNotAddException.class);
        }
    }

    @Test
    void charge_ExistingToken_Success() {
        String token = "tok_123456789";
        BigDecimal amount = BigDecimal.TEN;
        Charge charge = new Charge();
        charge.setId("pay_123456789");

        try (MockedStatic<Charge> chargeMockedStatic = Mockito.mockStatic(Charge.class)) {
            chargeMockedStatic.when(() -> Charge.create(anyMap()))
                    .thenReturn(charge);
            stripeServiceImpl.charge(token, amount);

            chargeMockedStatic.verify(() -> Charge.create(anyMap()));
        }
    }

    @Test
    void charge_UnExistingToken_Success() {
        String token = "tok_123456789";
        BigDecimal amount = BigDecimal.TEN;

        try (MockedStatic<Charge> chargeMockedStatic = Mockito.mockStatic(Charge.class)) {
            chargeMockedStatic.when(() -> Charge.create(anyMap()))
                    .thenThrow(RideNotPaidException.class);

            assertThatThrownBy(() -> stripeServiceImpl.charge(token, amount));
        }
    }

    @Test
    void getCustomer_ExistingCustomerIdAndEmail_Success() {
        String email = "test@test.com";
        String customerId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId("cus_123456789");

        Customer actualCustomer;
        try (MockedStatic<Customer> chargeMockedStatic = Mockito.mockStatic(Customer.class)) {
            chargeMockedStatic.when(() -> Customer.retrieve(any()))
                    .thenReturn(expectedCustomer);

            actualCustomer = stripeServiceImpl.getCustomer(customerId, email);

            chargeMockedStatic.verify(() -> Customer.retrieve(any()));
        }
        assertThat(actualCustomer.getId())
                .isEqualTo(expectedCustomer.getId());
    }

    @Test
    void getCustomer_UnExistingCustomerIdAndEmail_Success() {
        String email = "test@test.com";
        String customerId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId("cus_123456789");

        Customer actualCustomer;
        try (MockedStatic<Customer> chargeMockedStatic = Mockito.mockStatic(Customer.class)) {
            chargeMockedStatic.when(() -> Customer.retrieve(any()))
                    .thenThrow(RuntimeException.class);
            chargeMockedStatic.when(() -> Customer.create(anyMap()))
                    .thenReturn(expectedCustomer);

            actualCustomer = stripeServiceImpl.getCustomer(customerId, email);

            chargeMockedStatic.verify(() -> Customer.create(anyMap()));
        }
        assertThat(actualCustomer.getId())
                .isEqualTo(expectedCustomer.getId());
    }
}