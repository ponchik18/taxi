package com.modsen.service;

import com.modsen.dto.RatingListResponse;
import com.modsen.dto.RatingRequest;
import com.modsen.dto.RatingResponse;

public interface RatingService {

    RatingListResponse getAllRatingByEntityId(Long entityId);

    RatingResponse createRating(RatingRequest ratingRequest);

    RatingResponse getRatingById(long id);

    RatingResponse updateRating(long id, RatingRequest ratingRequest);

    void deleteRating(long id);
}