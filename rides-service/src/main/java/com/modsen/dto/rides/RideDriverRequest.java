package com.modsen.dto.rides;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    @Min(value = 1, message = RidesServiceConstants.Validation.Message.PASSENGER_ID_NOT_MIN)
    private Long passengerId;
    @NotEmpty(message = RidesServiceConstants.Validation.Message.PICK_UP_LOCATION_EMPTY)
    private String pickUpLocation;
    @NotEmpty(message = RidesServiceConstants.Validation.Message.DROP_LOCATION_EMPTY)
    private String dropLocation;
    //@NotNull(message = RidesServiceConstants.Validation.Message.START_TIME_EMPTY)
    private LocalDateTime startTime;
    //@NotNull(message = RidesServiceConstants.Validation.Message.END_TIME_EMPTY)
    private LocalDateTime endTime;
    //@DecimalMin(value = "0.0", inclusive = false, message = RidesServiceConstants.Validation.Message.COST_MORE_THAN_ZERO)
    private BigDecimal cost;
    //@NotEmpty(message = RidesServiceConstants.Validation.Message.STATUS_EMPTY)
    private String status;

    /*@AssertTrue(message = RidesServiceConstants.Validation.Message.END_TIME_IS_BEFORE_AFTER)
    public boolean isEndTimeAfterStartTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return endTime.isAfter(startTime);
    }*/
}