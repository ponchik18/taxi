package com.modsen.mapper;

import com.modsen.dto.PassengerRequest;
import com.modsen.dto.PassengerResponse;
import com.modsen.model.Passenger;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PassengerMapper {
    PassengerMapper MAPPER = Mappers.getMapper(PassengerMapper.class);

    Passenger mapToPassenger(@Valid PassengerRequest passengerRequest);
    PassengerResponse mapToPassengerResponse(Passenger passenger);
    List<PassengerResponse> mapToListOfPassengerResponse(List<Passenger> passengers);

}