package com.modsen.dto.rides;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidePassengerRequest {
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long passengerId;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String pickUpLocation;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String dropLocation;
}