package com.modsen.mapper;

import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.model.Passenger;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PassengerMapper {
    PassengerMapper MAPPER_INSTANCE = Mappers.getMapper(PassengerMapper.class);
    Passenger mapToPassenger(@Valid PassengerRequest passengerRequest);
    PassengerResponse mapToPassengerResponse(Passenger passenger);
    List<PassengerResponse> mapToListOfPassengerResponse(List<Passenger> passengers);
}