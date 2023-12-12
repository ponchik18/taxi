package com.modsen.dto.rating;

import com.modsen.constants.RidesServiceConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long entityId;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    @Min(value = 1, message = RidesServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    @Max(value = 5, message = RidesServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    private Integer mark;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private String userRole;
    @NotNull(message = RidesServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long rideId;
}