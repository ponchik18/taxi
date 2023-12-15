package com.modsen.promocodeservice.dto

import java.time.LocalDate

data class PromoCodeResponse(
    var id: Long,
    var name: String,
    var fromDate: LocalDate,
    var endDate: LocalDate,
    var discount: Int,
    var countOfUse: Int = 0
)