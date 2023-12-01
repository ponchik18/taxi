package com.modsen.repository;

import com.modsen.model.DriverBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverBalanceRepository extends JpaRepository<DriverBalance, Long> {
    Optional<DriverBalance> findByDriverId(Long driverId);
}