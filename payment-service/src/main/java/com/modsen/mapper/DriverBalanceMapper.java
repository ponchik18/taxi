package com.modsen.mapper;

import com.modsen.dto.balance.DriverBalanceResponse;
import com.modsen.model.DriverBalance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DriverBalanceMapper {
    DriverBalanceMapper MAPPER_INSTANCE = Mappers.getMapper(DriverBalanceMapper.class);

    DriverBalanceResponse mapToDriverBalanceResponse(DriverBalance driverBalance);
}