package com.modsen.promocodeservice.repository

import com.modsen.promocodeservice.model.PromoCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PromoCodeRepository: JpaRepository<PromoCode, Long>{
    fun findByName(name: String): PromoCode?
    fun existsByName(name: String): Boolean
    fun deleteByName(name: String)
}