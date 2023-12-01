package com.modsen.mapper;

import com.modsen.dto.DriverRequest;
import com.modsen.dto.DriverResponse;
import com.modsen.enums.DriverStatus;
import com.modsen.exception.DriverStatusNotFoundException;
import com.modsen.model.Driver;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DriverMapper {
    DriverMapper MAPPER_INSTANCE = Mappers.getMapper(DriverMapper.class);

    Driver mapToDriver(@Valid DriverRequest driverRequest);
    DriverResponse mapToDriverResponse(Driver driver);
    List<DriverResponse> mapToListOfDriverResponse(List<Driver> drivers);

    default DriverStatus mapStringToDriverStatus(String driverStatus) {
        try {
            return DriverStatus.valueOf(driverStatus);
        } catch (IllegalArgumentException exception) {
            throw new DriverStatusNotFoundException(driverStatus);
        }
    }

    default String mapDriverStatusToString(DriverStatus driverStatus) {
        return driverStatus.name();
    }
}