package com.modsen.repository;

import com.modsen.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}