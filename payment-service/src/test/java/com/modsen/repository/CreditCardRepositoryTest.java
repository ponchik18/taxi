package com.modsen.repository;

import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.enums.UserRole;
import com.modsen.model.CreditCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CreditCardRepositoryTest {
    private final List<CreditCard> creditCardList = new ArrayList<>();
    @Autowired
    private CreditCardRepository creditCardRepository;

    @BeforeEach
    void setUp() {
        creditCardList.add(
                CreditCard.builder()
                        .cardNumber(PaymentServiceTestConstants.TestData.CARD_NUMBER)
                        .cardHolder(PaymentServiceTestConstants.TestData.CARD_HOLDER)
                        .userRole(UserRole.PASSENGER)
                        .userId(PaymentServiceTestConstants.TestData.PASSENGER_ID)
                        .stripeCardId(PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1)
                        .build()
        );
        creditCardList.add(
                CreditCard.builder()
                        .cardNumber(PaymentServiceTestConstants.TestData.CARD_NUMBER)
                        .cardHolder(PaymentServiceTestConstants.TestData.CARD_HOLDER)
                        .userRole(UserRole.DRIVER)
                        .userId(PaymentServiceTestConstants.TestData.DRIVER_ID)
                        .stripeCardId(PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_2)
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
    }

    @Test
    public void findAllByUserIdAndUserRole_CreditCardExist_ReturnsCreditCard() {
        creditCardRepository.saveAll(creditCardList);
        List<CreditCard> actualCreditCards = creditCardRepository.findAllByUserIdAndUserRole(PaymentServiceTestConstants.TestData.PASSENGER_ID, UserRole.PASSENGER);

        assertThat(actualCreditCards)
                .isNotNull();
        assertThat(actualCreditCards.size())
                .isEqualTo(1);
        boolean allCreditCardEquals = actualCreditCards.stream()
                .allMatch(ride -> PaymentServiceTestConstants.TestData.PASSENGER_ID.equals(ride.getUserId()) ||
                        UserRole.PASSENGER.equals(ride.getUserRole()));

        assertThat(allCreditCardEquals)
                .isTrue();
    }

    @Test
    public void findByStripeCardId_CreditCardWithStripeIdExist_ReturnCreditCard() {
        creditCardRepository.saveAll(creditCardList);
        String stripeCustomerId = PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1;

        CreditCard actualCreditCard = creditCardRepository.findByStripeCardId(stripeCustomerId)
                .orElse(null);

        assertNotNull(actualCreditCard);
        assertThat(actualCreditCard.getStripeCardId())
                .isEqualTo(stripeCustomerId);
    }




}