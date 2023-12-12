package com.modsen.dto.rating;

import lombok.Builder;

import java.util.List;

@Builder
public record RatingListResponse(
        List<RatingResponse> ratingResponses,
        Double totalMark
) { }