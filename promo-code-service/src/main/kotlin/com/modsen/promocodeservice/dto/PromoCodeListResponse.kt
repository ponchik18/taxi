package com.modsen.promocodeservice.dto

data class PromoCodeListResponse(
    val promoCodes: List<PromoCodeResponse>,
    val countOfPromoCode: Int
)