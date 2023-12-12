package com.modsen.repository;

import com.modsen.enums.UserRole;
import com.modsen.model.DefaultCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultCreditCardRepository extends JpaRepository<DefaultCreditCard, Long> {
    Optional<DefaultCreditCard> findByUserIdAndUserRole(Long userId, UserRole userRole);
}