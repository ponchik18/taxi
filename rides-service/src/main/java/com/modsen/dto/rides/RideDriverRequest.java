package com.modsen.dto.rides;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long passengerId;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String pickUpLocation;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String dropLocation;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private LocalDateTime startTime;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private LocalDateTime endTime;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private BigDecimal cost;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String status;
}