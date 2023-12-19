package com.modsen.service.impl;

import com.modsen.constants.PassengerServiceConstants;
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

    private final String email = "passenger@mail.com";
    private final String phone = "+375111111111";
    @Mock
    private PassengerRepository passengerRepository;
    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void testGetAllPassenger() {
        // Given
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);

        List<Passenger> passengers = Collections.singletonList(new Passenger());
        Page<Passenger> page = new PageImpl<>(passengers);

        when(passengerRepository.findAll(pageable)).thenReturn(page);

        // When
        PassengerListResponse result = passengerService.getAllPassenger(pageSetting);

        // Then
        assertNotNull(result);
        assertEquals(passengers.size(), result.passengerCount());
        assertEquals(passengers.size(), result.passengers().size());
    }

    @Test
    void testSuccessfullyCreatePassenger() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();

        // Mock the repository behavior
        when(passengerRepository.save(any())).thenReturn(new Passenger());

        // When
        PassengerResponse result = passengerService.createPassenger(passengerRequest);

        // Then
        assertNotNull(result);
    }

    @Test
    void testDuplicateExceptionWhenCreatePassengerWithDuplicateEmail() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        String exceptionMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL, email);

        // When
        when(passengerRepository.existsByEmail(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.createPassenger(passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void testDuplicateExceptionWhenCreatePassengerWithDuplicatePhone() {
        // Given
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setPhone(phone);
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE, phone);

        // When
        when(passengerRepository.existsByPhone(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.createPassenger(passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void testSuccessfullyGetPassengerById() {
        // Given
        long passengerId = 1L;
        Passenger passenger = new Passenger();

        // When
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        PassengerResponse result = passengerService.getPassengerById(passengerId);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetPassengerByIdPassengerNotFound() {
        // Given
        long nonExistentPassengerId = 1L;
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.PASSENGER_NOT_FOUND, nonExistentPassengerId);

        // Mock the repository behavior
        when(passengerRepository.findById(nonExistentPassengerId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> passengerService.getPassengerById(nonExistentPassengerId))
                .isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void testSuccessfullyUpdatePassenger() {
        // Given
        long passengerId = 1L;
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(phone);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail(email);
        existingPassenger.setPhone(phone);

        // When
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(any())).thenReturn(new Passenger());
        PassengerResponse result = passengerService.updatePassenger(passengerId, passengerRequest);

        // Then
        assertNotNull(result);
    }

    @Test
    void testDuplicateExceptionWhenUpdatePassengerWithPhone() {
        // Given
        long passengerId = 1L;
        String passengerRequestPhone = "+0000000000";
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(passengerRequestPhone);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail(email);
        existingPassenger.setPhone(phone);
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE, passengerRequestPhone);

        // When
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByPhone(passengerRequest.getPhone())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.updatePassenger(passengerId, passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void testDuplicateExceptionWhenUpdatePassengerWithEmail() {
        // Given
        long passengerId = 1L;
        String passengerRequestEmail = "test@test.com";
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(passengerRequestEmail);
        Passenger existingPassenger = new Passenger();
        existingPassenger.setEmail(email);
        String exceptedMessage = String.format(PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL, passengerRequestEmail);

        // When
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByEmail(passengerRequest.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> passengerService.updatePassenger(passengerId, passengerRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    void testSuccessfullyDeletePassenger() {
        // Given
        long passengerId = 1L;

        // When
        when(passengerRepository.existsById(passengerId)).thenReturn(true);

        // Then
        assertDoesNotThrow(() -> passengerService.deletePassenger(passengerId));
        verify(passengerRepository, times(1)).deleteById(passengerId);
    }

    @Test
    void testDeletePassengerNotFound() {
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