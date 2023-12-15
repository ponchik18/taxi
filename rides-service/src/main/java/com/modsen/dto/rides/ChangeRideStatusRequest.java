package com.modsen.dto.rides;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRideStatusRequest {
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long rideId;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long driverId;
}