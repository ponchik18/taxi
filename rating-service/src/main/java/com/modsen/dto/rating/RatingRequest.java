package com.modsen.dto.rating;

import com.modsen.constants.RatingServiceConstants;
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
    @NotNull(message = RatingServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long entityId;
    @NotNull(message = RatingServiceConstants.Validation.Message.FIELD_EMPTY)
    @Min(value = 1, message = RatingServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    @Max(value = 5, message = RatingServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    private Integer mark;
    @NotNull(message = RatingServiceConstants.Validation.Message.FIELD_EMPTY)
    private String userRole;
    @NotNull(message = RatingServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long rideId;
}