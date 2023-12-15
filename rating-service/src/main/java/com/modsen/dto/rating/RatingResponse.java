package com.modsen.dto.rating;

import com.modsen.enums.UserRole;
import lombok.Builder;

@Builder
public record RatingResponse (
        Long entityId,
        Integer mark,
        UserRole userRole)
{ }