package com.modsen.service.impl;

import com.modsen.dto.RatingListResponse;
import com.modsen.dto.RatingRequest;
import com.modsen.dto.RatingResponse;
import com.modsen.exception.RatingNotFoundException;
import com.modsen.mapper.RatingMapper;
import com.modsen.model.Rating;
import com.modsen.repository.RatingRepository;
import com.modsen.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Override
    public RatingListResponse getAllRatingByEntityId(Long entityId) {
        List<Rating> ratingList;
        if(Objects.nonNull(entityId)) {
            ratingList = ratingRepository.findAllByEntityId(entityId);
        } else {
            ratingList = ratingRepository.findAll();
        }

        Double totalMark = ratingList.stream()
                .mapToDouble(Rating::getMark)
                .average()
                .orElse(0.0);

        return RatingListResponse.builder()
                .ratingResponses(RatingMapper.MAPPER_INSTANCE.mapToListOfRatingResponse(ratingList))
                .totalMark(totalMark)
                .build();
    }

    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        Rating newRating = RatingMapper.MAPPER_INSTANCE.mapToRating(ratingRequest);
        Rating savedRating = ratingRepository.save(newRating);
        return RatingMapper.MAPPER_INSTANCE.mapToRatingResponse(savedRating);
    }

    @Override
    public RatingResponse getRatingById(long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));

        return RatingMapper.MAPPER_INSTANCE.mapToRatingResponse(rating);
    }

    @Override
    public RatingResponse updateRating(long id, RatingRequest ratingRequest) {
        if(!ratingRepository.existsById(id)) {
            throw new RatingNotFoundException(id);
        }

        Rating rating = RatingMapper.MAPPER_INSTANCE.mapToRating(ratingRequest);
        rating.setEntityId(id);
        Rating updatedRating = ratingRepository.save(rating);
        return RatingMapper.MAPPER_INSTANCE.mapToRatingResponse(updatedRating);
    }

    @Override
    public void deleteRating(long id) {
        if(!ratingRepository.existsById(id)) {
            throw new RatingNotFoundException(id);
        }

        ratingRepository.deleteById(id);
    }
}