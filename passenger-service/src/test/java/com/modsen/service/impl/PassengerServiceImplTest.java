package com.modsen.service.impl;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.constants.PassengerServiceTestConstants;
import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.exception.PassengerNotFoundException;
import com.modsen.model.PageSetting;
import com.modsen.model.Passenger;
import com.modsen.repository.PassengerRepository;
import com.modsen.util.PageRequestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;
    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void getAllPassenger_ValidPageSetting_Success() {
        // Given
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);

        List<Passenger> expectedPassengers = Collections.singletonList(new Passenger());
        Page<Passenger> page = new PageImpl<>(expectedPassengers);

        when(passengerRepository.findAll(pageable))
                .thenReturn(page);

        // When
        PassengerListResponse actual = passengerService.getAllPassenger(pageSetting);

        // Then
        assertNotNull(actual);
        assertEquals(expectedPassengers.size(), actual.passengerCount());
        assertEquals(expectedPassengers.size(), actual.passengers().size());
    }

    @Test
    void createPassenger_ValidPassengerRequest_Success() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();

        // Mock the repository behavior
        when(passengerRepository.save(any())).thenReturn(new Passenger());

        // When
        PassengerResponse actual = passengerService.createPassenger(passengerRequest);

        // Then
        assertNotNull(actual);
    }

    @Test
    void createPassenger_DuplicateEmail_ExceptionThrown() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        String exceptionMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL, PassengerServiceTestConstants.TestData.EMAIL);

        // When
        when(passengerRepository.existsByEmail(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.createPassenger(passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void createPassenger_DuplicatePhone_ExceptionThrown() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setPhone(PassengerServiceTestConstants.TestData.PHONE);
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE, PassengerServiceTestConstants.TestData.PHONE);

        // When
        when(passengerRepository.existsByPhone(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.createPassenger(passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void getPassengerById_ExistingId_Success() {
        // Given
        long passengerId = 1L;
        Passenger passenger = new Passenger();

        // When
        when(passengerRepository.findById(passengerId))
                .thenReturn(Optional.of(passenger));
        PassengerResponse actual = passengerService.getPassengerById(passengerId);

        // Then
        assertNotNull(actual);
    }

    @Test
    void getPassengerById_NonExistingId_ExceptionThrown() {
        // Given
        long nonExistentPassengerId = 1L;
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, nonExistentPassengerId);

        // Mock the repository behavior
        when(passengerRepository.findById(nonExistentPassengerId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> passengerService.getPassengerById(nonExistentPassengerId))
                .isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void updatePassenger_ExistingIdAndValidRequest_Success() {
        // Given
        long passengerId = 1L;
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        passengerRequest.setPhone(PassengerServiceTestConstants.TestData.PHONE);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        existingPassenger.setPhone(PassengerServiceTestConstants.TestData.PHONE);
        PassengerResponse expectedPassengerResponse = PassengerResponse.builder()
                .email(PassengerServiceTestConstants.TestData.EMAIL)
                .phone(PassengerServiceTestConstants.TestData.PHONE)
                .build();

        // When
        when(passengerRepository.findById(passengerId))
                .thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(any()))
                .thenReturn(existingPassenger);
        PassengerResponse actual = passengerService.updatePassenger(passengerId, passengerRequest);

        // Then
        assertNotNull(actual);
        assertThat(actual).isEqualTo(expectedPassengerResponse);
    }

    @Test
    void updatePassenger_DuplicatePhone_ExceptionThrown() {
        // Given
        long passengerId = 1L;
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        passengerRequest.setPhone(PassengerServiceTestConstants.TestData.PHONE);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        existingPassenger.setPhone("+00000000000");
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE, PassengerServiceTestConstants.TestData.PHONE);

        // When
        when(passengerRepository.findById(passengerId))
                .thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByPhone(passengerRequest.getPhone()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.updatePassenger(passengerId, passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void updatePassenger_DuplicateEmail_ExceptionThrown() {
        // Given
        long passengerId = 1L;
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(PassengerServiceTestConstants.TestData.EMAIL);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail("test@gmail.com");
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL, PassengerServiceTestConstants.TestData.EMAIL);

        // When
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByEmail(passengerRequest.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.updatePassenger(passengerId, passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void deletePassenger_ExistingId_Success() {
        // Given
        long passengerId = 1L;

        // When
        when(passengerRepository.existsById(passengerId)).thenReturn(true);

        // Then
        assertDoesNotThrow(() -> passengerService.deletePassenger(passengerId));
        verify(passengerRepository, times(1)).deleteById(passengerId);
    }

    @Test
    void deletePassenger_NonExistingId_ExceptionThrown() {
        // Given
        long passengerId = 1L;
        String expectedMessage = String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, passengerId);

        // When
        when(passengerRepository.existsById(passengerId)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> passengerService.deletePassenger(passengerId))
                .isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }
}