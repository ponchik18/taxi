package com.modsen.repository;

import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.model.Ride;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class RideRepositoryTest {
    private final List<Ride> rideList = new ArrayList<>();
    @Autowired
    private RideRepository rideRepository;

    @BeforeEach
    void setUp() {
        rideList.add(
                Ride.builder()
                        .cost(BigDecimal.TEN)
                        .passengerId(RidesServiceTestConstants.TestData.PASSENGER_ID)
                        .driverId(RidesServiceTestConstants.TestData.DRIVER_ID_1)
                        .build()
        );
        rideList.add(
                Ride.builder()
                        .cost(BigDecimal.TEN)
                        .passengerId(RidesServiceTestConstants.TestData.PASSENGER_ID)
                        .driverId(RidesServiceTestConstants.TestData.DRIVER_ID_2)
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        rideRepository.deleteAll();
    }

    @Test
    void findAllByPassengerId_RidesExist_ReturnsRideList() {
        rideRepository.saveAll(rideList);
        List<Ride> actualRides = rideRepository.findAllByPassengerId(RidesServiceTestConstants.TestData.PASSENGER_ID);

        assertThat(actualRides).isNotNull();
        assertThat(actualRides.size()).isEqualTo(rideList.size());
        boolean allPassengerIdsEqual = actualRides.stream()
                .allMatch(ride -> RidesServiceTestConstants.TestData.PASSENGER_ID.equals(ride.getPassengerId()));
        assertThat(allPassengerIdsEqual).isTrue();
    }

    @Test
    void findByIdAndPassengerId_RidesExist_ReturnsRideList() {
        Long rideId = rideRepository.saveAll(rideList)
                .get(0)
                .getId();

        Ride actualRide = rideRepository.findByIdAndPassengerId(rideId, RidesServiceTestConstants.TestData.PASSENGER_ID)
                .orElse(null);

        assertNotNull(actualRide);
        assertThat(actualRide.getId()).isEqualTo(rideId);
        assertThat(actualRide.getPassengerId()).isEqualTo(RidesServiceTestConstants.TestData.PASSENGER_ID);
    }

    @Test
    void findByIdAndPassengerId_RidesNotExist_ReturnsEmptyRideList() {
        Long passengerId = 999L;
        rideRepository.saveAll(rideList);

        Ride actualRide = rideRepository.findByIdAndPassengerId(1L, passengerId)
                .orElse(null);

        assertNull(actualRide);
    }

    @Test
    void findByIdAndDriverId_RidesExist_ReturnsRideList() {
        Long rideId = rideRepository.saveAll(rideList)
                .get(0)
                .getId();

        Ride actualRide = rideRepository.findByIdAndDriverId(rideId, RidesServiceTestConstants.TestData.DRIVER_ID_1)
                .orElse(null);

        assertNotNull(actualRide);
        assertThat(actualRide.getId()).isEqualTo(rideId);
        assertThat(actualRide.getDriverId()).isEqualTo(RidesServiceTestConstants.TestData.DRIVER_ID_1);
    }

    @Test
    void findByIdAndDriverId_RidesNotExist_ReturnsEmptyRideList() {
        Long driverId = 999L;
        rideRepository.saveAll(rideList);

        Ride actualRide = rideRepository.findByIdAndDriverId(1L, driverId)
                .orElse(null);

        assertNull(actualRide);
    }
}