package com.modsen.dto.promo;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromoCodeResponse(
        Long id,
        String name,
        LocalDate fromDate,
        LocalDate endDate,
        Integer discount,
        Integer countOfUse) {
}