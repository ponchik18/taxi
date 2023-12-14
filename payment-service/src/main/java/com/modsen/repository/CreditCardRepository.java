package com.modsen.repository;

import com.modsen.enums.UserRole;
import com.modsen.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findAllByUserIdAndUserRole(Long userId, UserRole userRole);
    Optional<CreditCard> findByStripeCardId(String stripeCardId);
}