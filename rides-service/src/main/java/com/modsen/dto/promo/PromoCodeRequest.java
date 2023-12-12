package com.modsen.dto.promo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeRequest {
    private String name;
    private LocalDate fromDate;
    private LocalDate endDate;
    private Integer discount;
}