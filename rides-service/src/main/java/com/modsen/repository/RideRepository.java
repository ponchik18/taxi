package com.modsen.repository;

import com.modsen.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByPassengerId(Long passengerId);
    Optional<Ride> findByIdAndPassengerId(Long id, Long passengerId);
    Optional<Ride> findByIdAndDriverId(Long id, Long driverId);
}