package com.modsen.dto;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.enums.UserRole;
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
    @NotNull(message = RatingServiceConstants.Validation.Message.ENTITY_ID_EMPTY)
    @Min(value = 1, message = RatingServiceConstants.Validation.Message.ENTITY_ID_EMPTY)
    private Long entityId;
    @NotNull(message = RatingServiceConstants.Validation.Message.MARK_EMPTY)
    @Min(value = 1, message = RatingServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    @Max(value = 5, message = RatingServiceConstants.Validation.Message.MARK_NOT_RIGHT_RANGE)
    private Integer mark;
    @NotNull(message = RatingServiceConstants.Validation.Message.USER_ROLE_EMPTY)
    private UserRole userRole;


}