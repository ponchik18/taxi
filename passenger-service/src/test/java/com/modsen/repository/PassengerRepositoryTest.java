package com.modsen.repository;

import com.modsen.constants.PassengerServiceTestConstants;
import com.modsen.model.Passenger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = Passenger.builder()
                .email(PassengerServiceTestConstants.TestData.EMAIL)
                .phone(PassengerServiceTestConstants.TestData.PHONE)
                .build();
    }

    @AfterEach
    void tearDown() {
        passengerRepository.deleteAll();
    }

    @Test
    void existsByEmail_PassengerExists_ReturnsTrue() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByEmail(PassengerServiceTestConstants.TestData.EMAIL);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_PassengerNotExists_ReturnsFalse() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByEmail(PassengerServiceTestConstants.TestData.EMAIL);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByPhone_PassengerExists_ReturnsTrue() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByPhone(PassengerServiceTestConstants.TestData.PHONE);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByPhone_PassengerNotExists_ReturnsFalse() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByPhone(PassengerServiceTestConstants.TestData.PHONE);
        assertThat(exists).isFalse();
    }
}