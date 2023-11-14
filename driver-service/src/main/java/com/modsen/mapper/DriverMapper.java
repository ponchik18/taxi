package com.modsen.mapper;

import com.modsen.dto.DriverRequest;
import com.modsen.dto.DriverResponse;
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
}