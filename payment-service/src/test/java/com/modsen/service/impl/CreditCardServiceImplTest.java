package com.modsen.service.impl;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.dto.card.CreditCardListResponse;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.card.DefaultCreditCardRequest;
import com.modsen.dto.users.PassengerResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.CreditCardNotFoundException;
import com.modsen.exception.DefaultCardDeletionException;
import com.modsen.exception.StripeCustomerNotFoundException;
import com.modsen.model.CreditCard;
import com.modsen.model.StripeCustomer;
import com.modsen.repository.CreditCardRepository;
import com.modsen.repository.StripeCustomerRepository;
import com.modsen.service.StripeService;
import com.modsen.service.feigh.PassengerServiceClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class CreditCardServiceImplTest {

    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private StripeService stripeService;
    @Mock
    private PassengerServiceClient passengerServiceClient;
    @Mock
    private StripeCustomerRepository stripeCustomerRepository;
    @InjectMocks
    private CreditCardServiceImpl creditCardService;
    private CreditCardRequest creditCardRequest;
    private StripeCustomer stripeCustomer;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCardRequest = new CreditCardRequest();
        creditCardRequest.setCardHolder(PaymentServiceTestConstants.TestData.CARD_HOLDER);
        creditCardRequest.setCardNumber(PaymentServiceTestConstants.TestData.CARD_NUMBER);
        creditCardRequest.setExpMonth(PaymentServiceTestConstants.TestData.EXP_MONTH);
        creditCardRequest.setExpYear(PaymentServiceTestConstants.TestData.EXP_YEAR);
        creditCardRequest.setCvc(PaymentServiceTestConstants.TestData.CVC);
        creditCardRequest.setUserRole(UserRole.PASSENGER.name());
        creditCardRequest.setUserId(PaymentServiceTestConstants.TestData.PASSENGER_ID);

        stripeCustomer = new StripeCustomer();
        stripeCustomer.setUserId(1L);
        stripeCustomer.setUserRole(UserRole.DRIVER);
        stripeCustomer.setCustomerId(PaymentServiceTestConstants.TestData.CUSTOMER_ID);

        creditCard = CreditCard.builder()
                .id(PaymentServiceTestConstants.TestData.CARD_ID)
                .cardNumber(PaymentServiceTestConstants.TestData.CARD_NUMBER)
                .cardHolder(PaymentServiceTestConstants.TestData.CARD_HOLDER)
                .userRole(UserRole.DRIVER)
                .userId(PaymentServiceTestConstants.TestData.DRIVER_ID)
                .stripeCardId(PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_2)
                .build();
    }

    @Test
    void addCreditCard_ValidCreditCardRequest_Success() throws StripeException {
        String email = PaymentServiceTestConstants.TestData.EMAIL;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(PaymentServiceTestConstants.TestData.CUSTOMER_ID);
        String cardId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;
        CreditCard creditCard = new CreditCard();
        creditCard.setStripeCardId(cardId);

        when(passengerServiceClient.getPassengerById(anyLong()))
                .thenReturn(PassengerResponse.builder()
                        .email(email)
                        .build());
        when(stripeCustomerRepository.findByUserIdAndUserRole(anyLong(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(stripeService.createCardToken(any()))
                .thenReturn(new Token());
        when(stripeService.getCustomer(any(), any()))
                .thenReturn(expectedCustomer);
        when(stripeService.createCardForCustomer(any(), any()))
                .thenReturn(cardId);
        when(stripeCustomerRepository.save(any()))
                .thenReturn(stripeCustomer);
        when(creditCardRepository.save(any()))
                .thenReturn(creditCard);

        CreditCardResponse actualCreditCard = creditCardService.addCreditCard(creditCardRequest);

        assertNotNull(actualCreditCard);
        assertThat(actualCreditCard.stripeCardId())
                .isEqualTo(cardId);
        ArgumentCaptor<StripeCustomer> stripeCustomerArgumentCaptor = ArgumentCaptor.forClass(StripeCustomer.class);
        verify(stripeCustomerRepository, times(1)).save(stripeCustomerArgumentCaptor.capture());
        StripeCustomer actualStripeCustomer = stripeCustomerArgumentCaptor.getValue();
        assertThat(actualStripeCustomer.getCustomerId())
                .isEqualTo(expectedCustomer.getId());
        verify(creditCardRepository, times(1)).save(any());
    }

    @Test
    void addCreditCard_InvalidCreditCardRequest_Success() {
        String email = PaymentServiceTestConstants.TestData.EMAIL;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(PaymentServiceTestConstants.TestData.CUSTOMER_ID);
        String cardId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;
        CreditCard creditCard = new CreditCard();
        creditCard.setStripeCardId(cardId);

        when(passengerServiceClient.getPassengerById(anyLong()))
                .thenReturn(PassengerResponse.builder()
                        .email(email)
                        .build());
        when(stripeCustomerRepository.findByUserIdAndUserRole(anyLong(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(stripeService.createCardToken(any()))
                .thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> creditCardService.addCreditCard(creditCardRequest))
                .isInstanceOf(CreditCardNotAddException.class);
    }

    @Test
    void deleteCreditCard_ValidCardIdAndUserId_Success() throws StripeException {
        long cardId = PaymentServiceTestConstants.TestData.CARD_ID;
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        String userRole = UserRole.DRIVER.name();
        String stripeCardId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;

        when(creditCardRepository.findById(any()))
                .thenReturn(Optional.of(creditCard));
        when(stripeCustomerRepository.findByUserIdAndUserRole(anyLong(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(stripeService.getDefaultCardIdForCustomer(any()))
                .thenReturn(stripeCardId);

        creditCardService.deleteCreditCard(cardId, userId, userRole);

        verify(stripeService, times(1))
                .deleteCard(any(), any());
    }

    @Test
    void deleteCreditCard_DefaultCardDelete_ThrowException() throws StripeException {
        long cardId = PaymentServiceTestConstants.TestData.CARD_ID;
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        String userRole = UserRole.DRIVER.name();
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.DEFAULT_CREDIT_CARD_DELETION, cardId);

        when(creditCardRepository.findById(any()))
                .thenReturn(Optional.of(creditCard));
        when(stripeCustomerRepository.findByUserIdAndUserRole(anyLong(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(stripeService.getDefaultCardIdForCustomer(any()))
                .thenReturn(creditCard.getStripeCardId());

        assertThatThrownBy(() -> creditCardService.deleteCreditCard(cardId, userId, userRole))
                .isInstanceOf(DefaultCardDeletionException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void getCreditCardById_ExistingCardId_Success() {
        long creditCardId = creditCard.getId();
        when(creditCardRepository.findById(creditCardId)).thenReturn(Optional.of(creditCard));

        CreditCardResponse actualCreditCardResponse = creditCardService.getCreditCardById(creditCardId);

        assertThat(actualCreditCardResponse).usingRecursiveComparison()
                .isEqualTo(creditCard);
        verify(creditCardRepository, times(1))
                .findById(creditCardId);
    }

    @Test
    void getCreditCardById_UnExistingCardId_ThrowException() {
        long creditCardId = 1L;

        when(creditCardRepository.findById(creditCardId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> creditCardService.getCreditCardById(creditCardId));
        verify(creditCardRepository, times(1)).findById(creditCardId);
    }

    @Test
    public void getAllUserCreditCard_ExistingCard_Success() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        String userRole = UserRole.DRIVER.name();
        List<CreditCard> creditCardList = List.of(creditCard);

        when(creditCardRepository.findAllByUserIdAndUserRole(userId, UserRole.DRIVER))
                .thenReturn(creditCardList);
        CreditCardListResponse actualCreditCardListResponse = creditCardService.getAllUserCreditCard(userId, userRole);

        assertThat(actualCreditCardListResponse.countOfCard())
                .isEqualTo(creditCardList.size());
        boolean allCreditCardEquals = actualCreditCardListResponse.creditCardList()
                .stream()
                .allMatch(creditCard -> creditCard.userId().equals(userId)
                        || creditCard.userRole().equals(UserRole.DRIVER));
        assertThat(allCreditCardEquals)
                .isTrue();
    }

    @Test
    public void makeCreditCardDefault_ValidDefaultCreditCardRequest_Success() throws StripeException {
        DefaultCreditCardRequest defaultCreditCardRequest = new DefaultCreditCardRequest(PaymentServiceTestConstants.TestData.DRIVER_ID, UserRole.DRIVER.name(), PaymentServiceTestConstants.TestData.CARD_ID);

        when(stripeCustomerRepository.findByUserIdAndUserRole(any(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(creditCardRepository.findById(defaultCreditCardRequest.getCardId()))
                .thenReturn(Optional.of(creditCard));

        CreditCardResponse actualCreditCardResponse = creditCardService.makeCreditCardDefault(defaultCreditCardRequest);

        assertThat(actualCreditCardResponse).usingRecursiveComparison()
                .isEqualTo(creditCard);
        verify(creditCardRepository, times(1))
                .findById(defaultCreditCardRequest.getCardId());
        verify(stripeService, times(1))
                .makeCreditCardDefault(stripeCustomer.getCustomerId(), creditCard.getStripeCardId());
    }

    @Test
    public void makeCreditCardDefault_StripeCustomerNotFound_ThrowCustomerException() {
        DefaultCreditCardRequest defaultCreditCardRequest = new DefaultCreditCardRequest(PaymentServiceTestConstants.TestData.DRIVER_ID, UserRole.DRIVER.name(), PaymentServiceTestConstants.TestData.CARD_ID);
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.STRIPE_CUSTOMER_NOT_FOUND, defaultCreditCardRequest.getUserId(), defaultCreditCardRequest.getUserRole());

        when(stripeCustomerRepository.findByUserIdAndUserRole(any(), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> creditCardService.makeCreditCardDefault(defaultCreditCardRequest))
                .isInstanceOf(StripeCustomerNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void makeCreditCardDefault_CreditCardNotFound_ThrowException() {
        DefaultCreditCardRequest defaultCreditCardRequest = new DefaultCreditCardRequest(PaymentServiceTestConstants.TestData.DRIVER_ID, UserRole.DRIVER.name(), PaymentServiceTestConstants.TestData.CARD_ID);
        String expectedMessage = String.format(PaymentServiceConstants.Errors.Message.CREDIT_CARD_NOT_ADD, defaultCreditCardRequest.getCardId());

        when(stripeCustomerRepository.findByUserIdAndUserRole(any(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(creditCardRepository.findById(defaultCreditCardRequest.getCardId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> creditCardService.makeCreditCardDefault(defaultCreditCardRequest))
                .isInstanceOf(CreditCardNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void getDefaultCardForUser_ValidUserIdAndUserRole() {
        long userId = PaymentServiceTestConstants.TestData.DRIVER_ID;
        String userRole = UserRole.DRIVER.name();

        when(stripeCustomerRepository.findByUserIdAndUserRole(any(), any()))
                .thenReturn(Optional.of(stripeCustomer));
        when(creditCardRepository.findByStripeCardId(any()))
                .thenReturn(Optional.of(creditCard));

        CreditCardResponse actualCreditCardResponse = creditCardService.getDefaultCardForUser(userId, userRole);
        assertThat(actualCreditCardResponse).usingRecursiveComparison()
                .isEqualTo(creditCard);
    }
}