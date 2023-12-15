package com.modsen.repository;

import com.modsen.model.DriverBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverBalanceRepository extends JpaRepository<DriverBalance, Long> {
    Optional<DriverBalance> findByDriverId(Long driverId);
}