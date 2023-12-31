package com.modsen.service.impl;

import com.modsen.constants.DriverServiceConstants;
import com.modsen.constants.DriverServiceTestConstants;
import com.modsen.dto.driver.DriverChangeStatusForKafkaRequest;
import com.modsen.dto.driver.DriverListResponse;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import com.modsen.enums.DriverStatus;
import com.modsen.exception.DriverNotFoundException;
import com.modsen.model.Driver;
import com.modsen.model.PageSetting;
import com.modsen.repository.DriverRepository;
import com.modsen.util.PageRequestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {
    @Mock
    private DriverRepository driverRepository;
    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void getAllDriver_ValidPageSetting_Success() {
        // Given
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        Driver driver = new Driver();
        driver.setDriverStatus(DriverStatus.AVAILABLE);

        List<Driver> expectedDrivers = Collections.singletonList(driver);
        Page<Driver> page = new PageImpl<>(expectedDrivers);

        when(driverRepository.findAll(pageable))
                .thenReturn(page);

        // When
        DriverListResponse actual = driverService.getAllDrivers(pageSetting);

        // Then
        assertNotNull(actual);
        assertEquals(expectedDrivers.size(), actual.driverCount());
        assertEquals(expectedDrivers.size(), actual.drivers().size());
    }

    @Test
    void createDriver_ValidDriverRequest_Success() {
        // Given
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setDriverStatus(DriverStatus.AVAILABLE.name());
        Driver driver = new Driver();
        driver.setDriverStatus(DriverStatus.BUSY);

        // When
        when(driverRepository.save(any())).thenReturn(driver);
        DriverResponse actual = driverService.createDriver(driverRequest);

        // Then
        assertNotNull(actual);
    }

    @Test
    void createDriver_DuplicateEmail_ExceptionThrown() {
        // Given
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        String exceptionMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_EMAIL, DriverServiceTestConstants.TestData.EMAIL);

        // When
        when(driverRepository.existsByEmail(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.createDriver(driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void createDriver_DuplicatePhone_ExceptionThrown() {
        // Given
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setPhone(DriverServiceTestConstants.TestData.PHONE);
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_PHONE, DriverServiceTestConstants.TestData.PHONE);

        // When
        when(driverRepository.existsByPhone(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.createDriver(driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void createDriver_DuplicateLicenseNumber_ExceptionThrown() {
        // Given
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_LICENSE_NUMBER, DriverServiceTestConstants.TestData.LICENSE_NUMBER);

        // When
        when(driverRepository.existsByLicenseNumber(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.createDriver(driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void getDriverById_ExistingId_Success() {
        // Given
        long driverId = 1L;
        Driver driver = new Driver();
        driver.setDriverStatus(DriverStatus.AVAILABLE);

        // When
        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));
        DriverResponse actual = driverService.getDriverById(driverId);

        // Then
        assertNotNull(actual);
    }

    @Test
    void getDriverById_NonExistingId_ExceptionThrown() {
        // Given
        long nonExistentDriverId = 1L;
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DRIVER_NOT_FOUND, nonExistentDriverId);

        // Mock the repository behavior
        when(driverRepository.findById(nonExistentDriverId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> driverService.getDriverById(nonExistentDriverId))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void updateDriver_ExistingIdAndValidRequest_Success() {
        // Given
        long driverId = 1L;
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setDriverStatus(DriverStatus.AVAILABLE.name());
        driverRequest.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        driverRequest.setPhone(DriverServiceTestConstants.TestData.PHONE);
        driverRequest.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        Driver existingDriver = new Driver();
        existingDriver.setDriverStatus(DriverStatus.OFFLINE);
        existingDriver.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        existingDriver.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        existingDriver.setPhone(DriverServiceTestConstants.TestData.PHONE);
        DriverResponse expectedDriverResponse = DriverResponse.builder()
                .email(DriverServiceTestConstants.TestData.EMAIL)
                .phone(DriverServiceTestConstants.TestData.PHONE)
                .licenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER)
                .driverStatus(DriverStatus.OFFLINE.name())
                .build();

        // When
        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.save(any()))
                .thenReturn(existingDriver);
        DriverResponse actual = driverService.updateDriver(driverId, driverRequest);

        // Then
        assertNotNull(actual);
        assertThat(actual).isEqualTo(expectedDriverResponse);
    }

    @Test
    void updateDriver_DuplicatePhone_ExceptionThrown() {
        // Given
        long driverId = 1L;
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        driverRequest.setPhone(DriverServiceTestConstants.TestData.PHONE);
        driverRequest.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        Driver existingDriver = new Driver();
        existingDriver.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        existingDriver.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        existingDriver.setPhone("+00000000000");
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_PHONE, DriverServiceTestConstants.TestData.PHONE);

        // When
        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByPhone(driverRequest.getPhone()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.updateDriver(driverId, driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void updateDriver_DuplicateLicenseNumber_ExceptionThrown() {
        // Given
        long driverId = 1L;
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        driverRequest.setPhone(DriverServiceTestConstants.TestData.PHONE);
        driverRequest.setLicenseNumber(DriverServiceTestConstants.TestData.LICENSE_NUMBER);
        Driver existingDriver = new Driver();
        existingDriver.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        existingDriver.setLicenseNumber("lic_00000");
        existingDriver.setPhone(DriverServiceTestConstants.TestData.PHONE);
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_LICENSE_NUMBER, DriverServiceTestConstants.TestData.LICENSE_NUMBER);

        // When
        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByLicenseNumber(driverRequest.getLicenseNumber()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.updateDriver(driverId, driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void updateDriver_DuplicateEmail_ExceptionThrown() {
        // Given
        long driverId = 1L;
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(DriverServiceTestConstants.TestData.EMAIL);
        Driver existingDriver = new Driver();
        existingDriver.setEmail("test@gmail.com");
        String exceptedMessage = String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_EMAIL, DriverServiceTestConstants.TestData.EMAIL);

        // When
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByEmail(driverRequest.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> driverService.updateDriver(driverId, driverRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void deleteDriver_ExistingId_Success() {
        // Given
        long driverId = 1L;

        // When
        when(driverRepository.existsById(driverId)).thenReturn(true);

        // Then
        assertDoesNotThrow(() -> driverService.deleteDriver(driverId));
        verify(driverRepository, times(1)).deleteById(driverId);
    }

    @Test
    void deleteDriver_NonExistingId_ExceptionThrown() {
        // Given
        long driverId = 1L;
        String expectedMessage = String.format(DriverServiceConstants.Errors.Message.DRIVER_NOT_FOUND, driverId);

        // When
        when(driverRepository.existsById(driverId)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> driverService.deleteDriver(driverId))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    void changeDriverStatus_ValidDriverStatusChangeRequest_Success() {
        Driver existingDriver = new Driver();
        DriverStatusChangeRequest request = new DriverStatusChangeRequest(1L, DriverStatus.OFFLINE.name());

        when(driverRepository.findById(any())).thenReturn(Optional.of(existingDriver));
        when(driverRepository.save(any())).thenReturn(existingDriver);

        DriverResponse result = driverService.changeStatus(request);

        assertEquals(request.getStatus(), result.driverStatus());
        verify(driverRepository, times(1)).save(existingDriver);
    }

    @Test
    public void changeDriverStatusWithKafka_ValidDriverStatusChangeRequest_Success() {
        Driver existingDriver = new Driver();
        DriverChangeStatusForKafkaRequest request = new DriverChangeStatusForKafkaRequest(1L, DriverStatus.OFFLINE);
        when(driverRepository.findById(any())).thenReturn(Optional.of(existingDriver));

        driverService.changeDriverStatus(request);

        ArgumentCaptor<Driver> driverCapture = ArgumentCaptor.forClass(Driver.class);
        verify(driverRepository, times(1)).save(driverCapture.capture());
        assertThat(driverCapture.getValue().getDriverStatus())
                .isEqualTo(request.getDriverStatus());
    }
}