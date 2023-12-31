package com.modsen.repository;

import com.modsen.constants.RatingServiceTestConstants;
import com.modsen.enums.UserRole;
import com.modsen.model.Rating;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RatingRepositoryTest {
    @Autowired
    private RatingRepository ratingRepository;
    private Rating rating;

    @BeforeEach
    void setUp() {
        rating = Rating.builder()
                .entityId(RatingServiceTestConstants.TestData.ENTITY_ID)
                .mark(RatingServiceTestConstants.TestData.MARK)
                .userRole(UserRole.DRIVER)
                .rideId(RatingServiceTestConstants.TestData.RIDE_ID)
                .build();
    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
    }

    @Test
    void findAllByEntityIdAndUserRole_RatingExist_ReturnsRatingList() {
        ratingRepository.save(rating);

        List<Rating> actualRatingList = ratingRepository.findAllByEntityIdAndUserRole(
                RatingServiceTestConstants.TestData.ENTITY_ID,
                UserRole.DRIVER
        );
        assertThat(actualRatingList).isNotNull();
        assertThat(actualRatingList).hasSize(1);
    }

    @Test
    void findAllByEntityIdAndUserRole_RatingNotExist_ReturnsRatingEmptyList() {
        ratingRepository.save(rating);

        List<Rating> actualRatingList = ratingRepository.findAllByEntityIdAndUserRole(
                RatingServiceTestConstants.TestData.ENTITY_ID,
                UserRole.PASSENGER
        );
        assertThat(actualRatingList).isNotNull();
        assertThat(actualRatingList).hasSize(0);
    }

    @Test
    void existsByEntityIdAndUserRoleAndRideId_RatingExist_ReturnsTrue() {
        ratingRepository.save(rating);

        boolean exists = ratingRepository.existsByEntityIdAndUserRoleAndRideId(
                RatingServiceTestConstants.TestData.ENTITY_ID,
                UserRole.DRIVER,
                RatingServiceTestConstants.TestData.RIDE_ID
        );
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEntityIdAndUserRoleAndRideId_RatingNonExist_ReturnsFalse() {
        ratingRepository.save(rating);

        boolean exists = ratingRepository.existsByEntityIdAndUserRoleAndRideId(
                999L,
                UserRole.DRIVER,
                RatingServiceTestConstants.TestData.RIDE_ID
        );
        assertThat(exists).isFalse();
    }
}