package com.modsen.service.impl;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.constants.RatingServiceTestConstants;
import com.modsen.dto.entities.PassengerResponse;
import com.modsen.dto.entities.RideResponse;
import com.modsen.dto.rating.RatingListResponse;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.RatingAlreadyExistsException;
import com.modsen.exception.RatingNotFoundException;
import com.modsen.model.Rating;
import com.modsen.repository.RatingRepository;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.modsen.service.feigh.RidesServiceClient;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTest {
    private final List<Rating> existingRatings = new ArrayList<>();
    private final List<Integer> marks = new ArrayList<>() {
        {
            add(5);
            add(3);
        }
    };
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private PassengerServiceClient passengerServiceClient;
    @Mock
    private DriverServiceClient driverServiceClient;
    @Mock
    private RidesServiceClient ridesServiceClient;
    @InjectMocks
    private RatingServiceImpl ratingService;
    private RatingRequest ratingRequest;

    @BeforeEach
    public void setUp() {
        existingRatings.add(
                Rating.builder()
                        .userRole(UserRole.PASSENGER)
                        .mark(marks.get(0))
                        .entityId(RatingServiceTestConstants.TestData.ENTITY_ID)
                        .build()
        );
        existingRatings.add(
                Rating.builder()
                        .userRole(UserRole.PASSENGER)
                        .mark(marks.get(1))
                        .entityId(RatingServiceTestConstants.TestData.ENTITY_ID)
                        .build()
        );

        ratingRequest = new RatingRequest(RatingServiceTestConstants.TestData.ENTITY_ID, 5, UserRole.PASSENGER.name(), RatingServiceTestConstants.TestData.RIDE_ID);
    }

    @Test
    public void getAllRatingByEntityId_NotNullEntityIdAndUserRole_Success() {
        Double expectedTotalMark = marks.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);

        when(ratingRepository.findAllByEntityIdAndUserRole(any(), any())).thenReturn(existingRatings);
        RatingListResponse actualResponse = ratingService.getAllRatingByEntityId(RatingServiceTestConstants.TestData.ENTITY_ID, UserRole.PASSENGER.name());

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.ratingResponses().size()).isEqualTo(existingRatings.size());
        assertThat(actualResponse.totalMark()).isEqualTo(expectedTotalMark);
    }

    @Test
    public void getAllRatingByEntityId_NullEntityIdAndUserRole_Success() {
        Double expectedTotalMark = marks.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);

        when(ratingRepository.findAll()).thenReturn(existingRatings);
        RatingListResponse actualResponse = ratingService.getAllRatingByEntityId(null, null);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.ratingResponses().size()).isEqualTo(existingRatings.size());
        assertThat(actualResponse.totalMark()).isEqualTo(expectedTotalMark);
    }

    @Test
    public void createRating_ValidRatingRequest_Success() {
        when(ridesServiceClient.getRideById(ratingRequest.getRideId())).thenReturn(RideResponse.builder().build());
        when(passengerServiceClient.getPassengerById(ratingRequest.getEntityId())).thenReturn(PassengerResponse.builder().build());

        ratingService.createRating(ratingRequest);

        ArgumentCaptor<Rating> ratingArgumentCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(ratingArgumentCaptor.capture());
        Rating actualRating = ratingArgumentCaptor.getValue();
        assertThat(actualRating).usingRecursiveComparison()
                .ignoringFields("id", "userRole")
                .isEqualTo(ratingRequest);
    }

    @Test
    public void createRating_DuplicateRatingRequest_ThrowException() {
        String expectedMessage = String.format(RatingServiceConstants.Errors.Message.RATING_ALREADY_CREATED, ratingRequest.getUserRole(), ratingRequest.getRideId());

       when(ratingRepository.existsByEntityIdAndUserRoleAndRideId(any(), any(), any()))
               .thenReturn(true);

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest))
                .isInstanceOf(RatingAlreadyExistsException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void createRating_NotExistPassenger_ThrowException() {
        when(passengerServiceClient.getPassengerById(ratingRequest.getEntityId()))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void createRating_NotExistDriver_ThrowException() {
        ratingRequest.setUserRole(UserRole.DRIVER.name());

        when(driverServiceClient.getDriverById(ratingRequest.getEntityId()))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void createRating_NotExistRide_ThrowException() {
        when(ridesServiceClient.getRideById(ratingRequest.getRideId()))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void getRatingById_ExistingId_Success() {
        when(ratingRepository.findById(any()))
                .thenReturn(Optional.of(existingRatings.get(0)));

        RatingResponse actualRating = ratingService.getRatingById(1L);

        assertThat(actualRating).isNotNull();
    }

    @Test
    public void getRatingById_UnExistingId_ThrowException() {
        long driverId = 999L;
        String expectedMessage = String.format(RatingServiceConstants.Errors.Message.RATING_NOT_FOUND, driverId);

        assertThatThrownBy(() -> ratingService.getRatingById(driverId))
                .isInstanceOf(RatingNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void deleteRating_ExistingId_Success() {
        when(ratingRepository.findById(any()))
                .thenReturn(Optional.of(existingRatings.get(0)));

        RatingResponse actualRating = ratingService.getRatingById(1L);

        assertThat(actualRating).isNotNull();
    }

    @Test
    public void deleteRating_UnExistingId_ThrowException() {
        long driverId = 999L;
        String expectedMessage = String.format(RatingServiceConstants.Errors.Message.RATING_NOT_FOUND, driverId);

        assertThatThrownBy(() -> ratingService.getRatingById(driverId))
                .isInstanceOf(RatingNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }
}