package com.modsen.dto;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    @Min(value = 1, message = RidesServiceConstants.Validation.Message.PASSENGER_ID_NOT_MIN)
    private Long passengerId;
    @Min(value = 1, message = RidesServiceConstants.Validation.Message.DRIVER_ID_NOT_MIN)
    private Long driverId;
    @NotEmpty(message = RidesServiceConstants.Validation.Message.PICK_UP_LOCATION_EMPTY)
    private String pickUpLocation;
    @NotEmpty(message = RidesServiceConstants.Validation.Message.DROP_LOCATION_EMPTY)
    private String dropLocation;
    @NotNull(message = RidesServiceConstants.Validation.Message.START_TIME_EMPTY)
    private LocalDateTime startTime;
    @NotNull(message = RidesServiceConstants.Validation.Message.END_TIME_EMPTY)
    private LocalDateTime endTime;
    @DecimalMin(value = "0.0", inclusive = false, message = RidesServiceConstants.Validation.Message.COST_MORE_THAN_ZERO)
    private BigDecimal cost;
    @NotEmpty(message = RidesServiceConstants.Validation.Message.STATUS_EMPTY)
    private String status;

    @AssertTrue(message = RidesServiceConstants.Validation.Message.END_TIME_IS_BEFORE_AFTER)
    public boolean isEndTimeAfterStartTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return endTime.isAfter(startTime);
    }
}