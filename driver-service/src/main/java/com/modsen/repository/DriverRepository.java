package com.modsen.repository;

import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Driver> findAllByDriverStatus(DriverStatus driverStatus);
}