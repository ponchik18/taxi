package com.modsen.service.impl;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.PassengerListResponse;
import com.modsen.dto.PassengerRequest;
import com.modsen.dto.PassengerResponse;
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
                .totalPassengerCount(passengerResponses.size())
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
        if(!passengerRepository.existsById(id)){
            throw new PassengerNotFoundException(id);
        }

        validatePassengerRequest(passengerRequest);
        Passenger updatedPassenger = PassengerMapper.MAPPER_INSTANCE.mapToPassenger(passengerRequest);
        updatedPassenger.setId(id);

        return PassengerMapper.MAPPER_INSTANCE.mapToPassengerResponse(
                    passengerRepository.save(updatedPassenger)
        );
    }

    public void deletePassenger(long id) {
        if(passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
        } else {
            throw new PassengerNotFoundException(id);
        }
    }

    private void validatePassengerRequest(PassengerRequest passengerRequest) {
        if(passengerRepository.existsByEmail(passengerRequest.getEmail())) {
            throw new DuplicateKeyException(
                    String.format(
                            PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_EMAIL,
                            passengerRequest.getEmail()
                    )
            );
        }

        if(passengerRepository.existsByPhone(passengerRequest.getPhone())) {
            throw new DuplicateKeyException(
                    String.format(
                            PassengerServiceConstants.Errors.Message.DUPLICATE_PASSENGER_WITH_PHONE,
                            passengerRequest.getPhone()
                    )
            );
        }
    }
}