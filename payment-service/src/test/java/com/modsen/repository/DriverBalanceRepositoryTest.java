package com.modsen.repository;

import com.modsen.constants.PaymentServiceTestConstants;
import com.modsen.model.DriverBalance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class DriverBalanceRepositoryTest {

    private final List<DriverBalance> driverBalanceList = new ArrayList<>();
    @Autowired
    private DriverBalanceRepository driverBalanceRepository;

    @BeforeEach
    void setUp() {
        driverBalanceList.add(
                DriverBalance.builder()
                        .amount(BigDecimal.TEN)
                        .driverId(PaymentServiceTestConstants.TestData.DRIVER_ID)
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        driverBalanceRepository.deleteAll();
    }

    @Test
    public void findByDriverId_DriverExist_Success() {
        driverBalanceRepository.saveAll(driverBalanceList);
        Long driverId = PaymentServiceTestConstants.TestData.DRIVER_ID;

        DriverBalance actualDriverBalance = driverBalanceRepository.findByDriverId(driverId)
                .orElse(null);

        assertNotNull(actualDriverBalance);
        assertThat(actualDriverBalance.getDriverId())
                .isEqualTo(driverId);
    }

    @Test
    public void findByDriverId_UnDriverExist_NotFound() {
        driverBalanceRepository.saveAll(driverBalanceList);

        DriverBalance actualDriverBalance = driverBalanceRepository.findByDriverId(999L)
                .orElse(null);

        assertNull(actualDriverBalance);
    }
}