package com.modsen.promocodeservice.service.impl

import com.modsen.promocodeservice.constants.PromoCodeServiceConstant
import com.modsen.promocodeservice.dto.PromoCodeListResponse
import com.modsen.promocodeservice.dto.PromoCodeRequest
import com.modsen.promocodeservice.dto.PromoCodeResponse
import com.modsen.promocodeservice.exception.PromoCodeNotFountException
import com.modsen.promocodeservice.mapper.PromoCodeMapper
import com.modsen.promocodeservice.repository.PromoCodeRepository
import com.modsen.promocodeservice.service.PromoCodeService
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class PromoCodeServiceImpl(val promoCodeRepository: PromoCodeRepository, val promoCodeMapper: PromoCodeMapper) :
    PromoCodeService {
    override fun getAllPromoCode(): PromoCodeListResponse {
        val promoCodes = promoCodeRepository.findAll()
        val promoCodeResponseList = promoCodeMapper.mapToListOfPromoCodeResponse(promoCodes)
        return PromoCodeListResponse(
            promoCodeResponseList, promoCodeResponseList.size
        )
    }

    override fun getPromoCodeByName(name: String): PromoCodeResponse {
        val promoCode = promoCodeRepository.findByName(name) ?: throw PromoCodeNotFountException(name)
        return promoCodeMapper.mapToPromoCodeResponse(promoCode)
    }

    @Transactional
    override fun deleteByName(name: String) {
        if (promoCodeRepository.existsByName(name)) {
            promoCodeRepository.deleteByName(name)
        } else {
            throw PromoCodeNotFountException(name)
        }
    }

    override fun updatePromoCode(name: String, promoCodeRequest: PromoCodeRequest): PromoCodeResponse {
        val promoCode = promoCodeRepository.findByName(name) ?: throw PromoCodeNotFountException(name)
        if(promoCodeRequest.name != promoCode.name){
            validateUniqueName(promoCodeRequest.name)
        }
        val updatePromoCode = promoCodeMapper.mapToPromoCode(promoCodeRequest)
        updatePromoCode.id = promoCode.id
        updatePromoCode.countOfUse = promoCode.countOfUse
        return promoCodeMapper.mapToPromoCodeResponse(
            promoCodeRepository.save(updatePromoCode)
        )
    }

    override fun createPromoCode(promoCodeRequest: PromoCodeRequest): PromoCodeResponse {
        validateUniqueName(promoCodeRequest.name)
        val createdPromoCode = promoCodeMapper.mapToPromoCode(promoCodeRequest)
        return promoCodeMapper.mapToPromoCodeResponse(
            promoCodeRepository.save(createdPromoCode)
        )
    }

    override fun applyPromoCode(name: String): PromoCodeResponse {
        val promoCode = promoCodeRepository.findByName(name) ?: throw PromoCodeNotFountException(name)
        val nowDate = LocalDate.now()
        if(nowDate.isBefore(promoCode.fromDate) && nowDate.isAfter(promoCode.endDate)){
            throw PromoCodeNotFountException(name)
        }
        promoCode.countOfUse+=1
        return promoCodeMapper.mapToPromoCodeResponse(promoCodeRepository.save(promoCode))
    }

    private fun validateUniqueName(name: String) {
        if (promoCodeRepository.existsByName(name)) {
            throw DuplicateKeyException(PromoCodeServiceConstant.Errors.Message.DUPLICATE_NAME.format(name))
        }
    }
}