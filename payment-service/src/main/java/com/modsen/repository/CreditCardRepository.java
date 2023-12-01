package com.modsen.repository;

import com.modsen.enums.UserRole;
import com.modsen.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findAllByUserIdAndUserRole(Long userId, UserRole userRole);
}