package com.modsen.repository;


import com.modsen.enums.UserRole;
import com.modsen.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByEntityIdAndUserRole(Long entityId, UserRole userRole);
    boolean existsByEntityIdAndUserRoleAndRideId(Long entityId, UserRole userRole, Long rentId);
}