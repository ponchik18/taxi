package com.modsen.repository;

import com.modsen.constants.DriverServiceTestConstants;
import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class DriverRepositoryTest {
    @Autowired
    private DriverRepository driverRepository;
    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = Driver.builder()
                .email(DriverServiceTestConstants.TestData.EMAIL)
                .phone(DriverServiceTestConstants.TestData.PHONE)
                .licenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER)
                .driverStatus(DriverStatus.BUSY)
                .build();
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
    }

    @Test
    void existsByEmail_DriverExists_ReturnsTrue() {
        driverRepository.save(driver);

        boolean exists = driverRepository.existsByEmail(DriverServiceTestConstants.TestData.EMAIL);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_DriverNotExists_ReturnsFalse() {
        boolean exists = driverRepository.existsByEmail(DriverServiceTestConstants.TestData.EMAIL);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByPhone_DriverExists_ReturnsTrue() {
        driverRepository.save(driver);

        boolean exists = driverRepository.existsByPhone(DriverServiceTestConstants.TestData.PHONE);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByPhone_DriverNotExists_ReturnsFalse() {
        boolean exists = driverRepository.existsByPhone(DriverServiceTestConstants.TestData.PHONE);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByLicenceNumber_DriverNotExists_ReturnsFalse() {
        boolean exists = driverRepository.existsByEmail(DriverServiceTestConstants.TestData.EMAIL);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByLicenceNumber_DriverExists_ReturnsTrue() {
        driverRepository.save(driver);

        boolean exists = driverRepository.existsByLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        assertThat(exists).isTrue();
    }

    @Test
    void findAllByDriverStatus_DriverStatusBusy_Success() {
        driverRepository.save(driver);

        List<Driver> driverList = driverRepository.findAllByDriverStatus(DriverStatus.BUSY);
        assertThat(driverList.size()).isGreaterThan(0);
    }

    @Test
    void findAllByDriverStatus_DriverStatusBusy_NotFound() {
        driverRepository.save(driver);

        List<Driver> driverList = driverRepository.findAllByDriverStatus(DriverStatus.OFFLINE);
        assertThat(driverList.size()).isEqualTo(0);
    }
}