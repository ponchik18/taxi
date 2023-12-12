package com.modsen.promocodeservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
data class PromoCode(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    var name: String,
    var fromDate: LocalDate,
    var endDate: LocalDate,
    var discount: Int,
    var countOfUse:Int = 0
)