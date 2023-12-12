package com.modsen.promocodeservice.service

import com.modsen.promocodeservice.dto.PromoCodeListResponse
import com.modsen.promocodeservice.dto.PromoCodeRequest
import com.modsen.promocodeservice.dto.PromoCodeResponse

interface PromoCodeService {
    fun getAllPromoCode(): PromoCodeListResponse
    fun getPromoCodeByName(name: String): PromoCodeResponse
    fun deleteByName(name: String)
    fun updatePromoCode(name: String, promoCodeRequest: PromoCodeRequest): PromoCodeResponse
    fun createPromoCode(promoCodeRequest: PromoCodeRequest): PromoCodeResponse
    fun applyPromoCode(name: String): PromoCodeResponse
}