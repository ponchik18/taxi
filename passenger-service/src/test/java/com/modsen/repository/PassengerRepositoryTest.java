package com.modsen.repository;

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
    private final String email = "passenger@mail.com";
    private final String phone = "+375111111111";
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = Passenger.builder()
                .email(email)
                .phone(phone)
                .build();
    }

    @AfterEach
    void tearDown() {
        passengerRepository.deleteAll();
    }

    @Test
    void testIsPassengerExistsByEmail() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByEmail(email);
        assertThat(exists).isTrue();
    }

    @Test
    void testIsPassengerNotExistsByEmail() {
        String searchedEmail = "test@mail.com";
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByEmail(searchedEmail);
        assertThat(exists).isFalse();
    }

    @Test
    void testIsPassengerExistsByPhone() {
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByPhone(phone);
        assertThat(exists).isTrue();
    }

    @Test
    void testIsPassengerNotExistsByPhone() {
        String searchedPhone = "+777777777777";
        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByPhone(searchedPhone);
        assertThat(exists).isFalse();
    }
}