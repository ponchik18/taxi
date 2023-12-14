package com.modsen.repository;

import com.modsen.enums.UserRole;
import com.modsen.model.StripeCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Long> {
    Optional<StripeCustomer> findByUserIdAndUserRole(Long userId, UserRole userRole);
}