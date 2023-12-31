package com.modsen.repository;

import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.enums.UserRole;
import com.modsen.model.StripeCustomer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class StripeCustomerRepositoryTest {

    private final List<StripeCustomer> stripeCustomerList = new ArrayList<>();
    @Autowired
    private StripeCustomerRepository stripeCustomerRepository;

    @BeforeEach
    void setUp() {
        stripeCustomerList.add(
                StripeCustomer.builder()
                        .userId(PaymentServiceTestConstants.TestData.DRIVER_ID)
                        .userRole(UserRole.DRIVER)
                        .customerId(PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_1)
                        .build()
        );
        stripeCustomerList.add(
                StripeCustomer.builder()
                        .userId(PaymentServiceTestConstants.TestData.PASSENGER_ID)
                        .userRole(UserRole.PASSENGER)
                        .customerId(PaymentServiceTestConstants.TestData.STRIPE_CARD_ID_2)
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        stripeCustomerRepository.deleteAll();
    }

    @Test
    public void findByUserIdAndUserRole_ExistStripeRide_Success() {
        Long userId = PaymentServiceTestConstants.TestData.PASSENGER_ID;
        stripeCustomerRepository.saveAll(stripeCustomerList);

        StripeCustomer actualStripeCustomer = stripeCustomerRepository.findByUserIdAndUserRole(userId, UserRole.PASSENGER)
                .orElse(null);

        assertNotNull(actualStripeCustomer);
        assertThat(actualStripeCustomer).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(stripeCustomerList.get(1));
    }

    @Test
    public void findByUserIdAndUserRole_UnExistStripeRide_NotFound() {
        stripeCustomerRepository.saveAll(stripeCustomerList);

        StripeCustomer actualStripeCustomer = stripeCustomerRepository.findByUserIdAndUserRole(999L, UserRole.PASSENGER)
                .orElse(null);

        assertNull(actualStripeCustomer);
    }
}