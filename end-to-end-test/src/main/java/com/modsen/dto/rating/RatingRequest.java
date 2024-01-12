package com.modsen.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    private Long entityId;
    private Integer mark;
    private String userRole;
    private Long rideId;
}