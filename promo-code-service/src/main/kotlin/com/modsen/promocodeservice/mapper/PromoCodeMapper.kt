package com.modsen.promocodeservice.mapper

import com.modsen.promocodeservice.dto.PromoCodeRequest
import com.modsen.promocodeservice.dto.PromoCodeResponse
import com.modsen.promocodeservice.model.PromoCode
import org.springframework.stereotype.Component

@Component
class PromoCodeMapper {
    fun mapToPromoCode(promoCodeRequest: PromoCodeRequest) = PromoCode(
        id = 0,
        name = promoCodeRequest.name,
        fromDate = promoCodeRequest.fromDate,
        endDate = promoCodeRequest.endDate,
        discount = promoCodeRequest.discount,
        countOfUse = 0
    )

    fun mapToPromoCodeResponse(promoCode: PromoCode) = PromoCodeResponse(
        id = promoCode.id,
        name = promoCode.name,
        fromDate = promoCode.fromDate,
        endDate = promoCode.endDate,
        discount = promoCode.discount,
        countOfUse = promoCode.countOfUse
    )

    fun mapToListOfPromoCodeResponse(promoCodeList: List<PromoCode>) = promoCodeList.map { mapToPromoCodeResponse(it) }
}