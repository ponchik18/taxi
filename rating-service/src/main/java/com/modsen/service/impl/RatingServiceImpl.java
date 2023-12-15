package com.modsen.service.impl;

import com.modsen.dto.rating.RatingListResponse;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.RatingAlreadyExistsException;
import com.modsen.exception.RatingNotFoundException;
import com.modsen.mapper.RatingMapper;
import com.modsen.mapper.UserRoleMapper;
import com.modsen.model.Rating;
import com.modsen.repository.RatingRepository;
import com.modsen.service.RatingService;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.modsen.service.feigh.RidesServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final PassengerServiceClient passengerServiceClient;
    private final DriverServiceClient driverServiceClient;
    private final RidesServiceClient ridesServiceClient;

    @Override
    public RatingListResponse getAllRatingByEntityId(Long entityId, String userRole) {
        List<Rating> ratingList = Objects.nonNull(entityId) || Objects.nonNull(userRole)
                ? ratingRepository.findAllByEntityIdAndUserRole(entityId, UserRoleMapper.mapToUserRole(userRole))
                : ratingRepository.findAll();

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
        validateRatingRequest(ratingRequest);
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
    public void deleteRating(long id) {
        if (!ratingRepository.existsById(id)) {
            throw new RatingNotFoundException(id);
        }

        ratingRepository.deleteById(id);
    }

    private void validateRatingRequest(RatingRequest ratingRequest) {
        UserRole userRole = UserRoleMapper.mapToUserRole(ratingRequest.getUserRole());
        if (ratingRepository.existsByEntityIdAndUserRoleAndRideId(ratingRequest.getEntityId(), userRole, ratingRequest.getRideId())) {
            throw new RatingAlreadyExistsException(ratingRequest.getRideId(), userRole);
        }
        validateRide(ratingRequest.getRideId());
        switch (userRole) {
            case DRIVER -> validateDriver(ratingRequest.getEntityId());
            case PASSENGER -> validatePassenger(ratingRequest.getEntityId());
        }
    }

    private void validateRide(Long id) {
        ridesServiceClient.getRideById(id);
    }

    private void validateDriver(Long id) {
        driverServiceClient.getDriverById(id);
    }

    private void validatePassenger(Long id) {
        passengerServiceClient.getPassengerById(id);
    }
}