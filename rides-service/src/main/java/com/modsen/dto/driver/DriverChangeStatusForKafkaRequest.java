package com.modsen.dto.driver;

import com.modsen.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeStatusForKafkaRequest {
    private Long driverId;
    private DriverStatus driverStatus;
}