package com.modsen.dto.driver;

import com.modsen.constants.DriverServiceConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusChangeRequest {
    @NotNull(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    @Min(value = 0, message = DriverServiceConstants.Validation.Message.VALUE_LESS_ZERO)
    private Long driverId;
    private String status;
}