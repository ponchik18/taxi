package com.modsen.service.impl;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    @Override
    public List<PassengerResponse> getAllPassenger(PageSetting pageSetting) {
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        passengerRepository.findAll(pageable).forEach(System.out::println);
        List<Passenger> passengerList = passengerRepository.findAll(pageable).toList();
        return PassengerMapper.MAPPER.mapToListOfPassengerResponse(passengerList);
    }

    @Override
    public void createPassenger(PassengerRequest passengerRequest) {
        Passenger newPassenger = PassengerMapper.MAPPER.mapToPassenger(passengerRequest);
        passengerRepository.save(newPassenger);
    }

    @Override
    public PassengerResponse getPassengerById(Integer id) {
        Passenger foundPassenger = passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException(id));
        return PassengerMapper.MAPPER.mapToPassengerResponse(foundPassenger);
    }

    @Override
    public void updatePassenger(Integer id, PassengerRequest passengerRequest) {
        Passenger updatedPassenger = PassengerMapper.MAPPER.mapToPassenger(passengerRequest);
        updatedPassenger.setId(id);
        passengerRepository.save(updatedPassenger);
    }

    public void deletePassenger(Integer id) {
        passengerRepository.deleteById(id);
    }
}