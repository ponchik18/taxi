package com.modsen.dto.promo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeApplyRequest {
    private Long rideId;
    private Long passengerId;
    private String promoCode;
}