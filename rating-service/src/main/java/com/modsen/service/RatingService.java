package com.modsen.service;

import com.modsen.dto.rating.RatingListResponse;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;

public interface RatingService {

    RatingListResponse getAllRatingByEntityId(Long entityId, String userRole);

    RatingResponse createRating(RatingRequest ratingRequest);

    RatingResponse getRatingById(long id);

    void deleteRating(long id);
}