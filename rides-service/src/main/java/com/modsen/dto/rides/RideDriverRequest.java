package com.modsen.dto.rides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideDriverRequest implements Serializable {
    private Long passengerId;
    private String pickUpLocation;
    private String dropLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal cost;
    private String status;
}