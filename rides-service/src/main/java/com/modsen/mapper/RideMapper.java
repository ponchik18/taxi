package com.modsen.mapper;

import com.modsen.dto.RideRequest;
import com.modsen.dto.RideResponse;
import com.modsen.enums.RideStatus;
import com.modsen.model.Ride;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RideMapper {
    RideMapper MAPPER_INSTANCE = Mappers.getMapper(RideMapper.class);

    Ride mapToRide(@Valid RideRequest rideRequest);

    RideResponse mapToRideResponse(Ride ride);

    List<RideResponse> mapToListOfRideResponse(List<Ride> rides);

    default RideStatus mapStringToRideStatus(String status) {
        return RideStatus.valueOf(status);
    }

    default String mapRideStatusToString(RideStatus rideStatus) {
        return rideStatus.name();
    }
}