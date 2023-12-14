package com.modsen.service.impl;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.exception.PassengerNotFoundException;
import com.modsen.mapper.PassengerMapper;
import com.modsen.model.PageSetting;
import com.modsen.model.Passenger;
import com.modsen.repository.PassengerRepository;
import com.modsen.service.PassengerService;
import com.modsen.util.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    @Override
    public PassengerListResponse getAllPassenger(PageSetting pageSetting) {
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        List<Passenger> passengers = passengerRepository.findAll(pageable)
                .toList();
        List<PassengerResponse> passengerResponses = PassengerMapper.MAPPER_INSTANCE
                .mapToListOfPassengerResponse(passengers);

        return PassengerListResponse.builder()
                .passengers(passengerResponses)
                .passengerCount(passengerResponses.size())
                .build();
    }

    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        validatePassengerRequest(passengerRequest);
        Passenger newPassenger = PassengerMapper.MAPPER_INSTANCE
                .mapToPassenger(passengerRequest);

        return PassengerMapper.MAPPER_INSTANCE.mapToPassengerResponse(
                passengerRepository.save(newPassenger)
        );
    }

    @Override
    public PassengerResponse getPassengerById(long id) {
        Passenger foundPassenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id));

        return PassengerMapper.MAPPER_INSTANCE.mapToPassengerResponse(foundPassenger);
    }

    @Override
    public PassengerResponse updatePassenger(long id, PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id));
        validatePassengerUpdateRequest(passengerRequest, passenger);

        Passenger updatedPassenger = PassengerMapper.MAPPER_INSTANCE.mapToPassenger(passengerRequest);
        updatedPassenger.setId(id);

        return PassengerMapper.MAPPER_INSTANCE.mapToPassengerResponse(
                passengerRepository.save(updatedPassenger)
        );
    }

    @Override
    public void deletePassenger(long id) {
        if (!passengerRepository.existsById(id)) {
            throw new PassengerNotFoundException(id);
        }

        passengerRepository.deleteById(id);
    }

    private void validatePassengerUpdateRequest(PassengerRequest passengerRequest, Passenger passenger) {
        if (!passenger.getEmail().equals(passengerRequest.getEmail())) {
            validatePassengerRequestByEmail(passengerRequest);
        }
        if (!passenger.getPhone().equals(passengerRequest.getPhone())) {
            validatePassengerRequestByPhone(passengerRequest);
        }
    }

    private void validatePassengerRequest(PassengerRequest passengerRequest) {
        validatePassengerRequestByEmail(passengerRequest);

        validatePassengerRequestByPhone(passengerRequest);
    }

    private void validatePassengerRequestByPhone(PassengerRequest passengerRequest) {
        if (passengerRepository.existsByPhone(passengerRequest.getPhone())) {
            String error = String.format(
                    PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE,
                    passengerRequest.getPhone()
            );
            throw new DuplicateKeyException(error);
        }
    }

    private void validatePassengerRequestByEmail(PassengerRequest passengerRequest) {
        if (passengerRepository.existsByEmail(passengerRequest.getEmail())) {
            String error = String.format(
                    PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL,
                    passengerRequest.getEmail()
            );
            throw new DuplicateKeyException(error);
        }
    }
}