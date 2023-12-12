package com.modsen.model;

import com.modsen.enums.RideStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long passengerId;
    private Long driverId;
    private String pickUpLocation;
    private String dropLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private RideStatus status;
    private Boolean isPromoCodeApplied = Boolean.FALSE;
}