package com.modsen.promocodeservice.controller

import com.modsen.promocodeservice.constants.PromoCodeServiceConstant
import com.modsen.promocodeservice.dto.PromoCodeRequest
import com.modsen.promocodeservice.service.PromoCodeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PromoCodeServiceConstant.BasePath.PROMO_CODE_CONTROLLER_PATH)
class PromoCodeController(val promoCodeService: PromoCodeService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPromoCode() = promoCodeService.getAllPromoCode()

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    fun getPromoCodeById(@PathVariable name: String) = promoCodeService.getPromoCodeByName(name)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    fun createPromoCode(@RequestBody promoCodeRequest: PromoCodeRequest) =
        promoCodeService.createPromoCode(promoCodeRequest)

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    fun updatePromoCode(@PathVariable name: String, @Valid @RequestBody promoCodeRequest: PromoCodeRequest) =
        promoCodeService.updatePromoCode(name, promoCodeRequest)

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    fun deletePromoCode(@PathVariable name: String) = promoCodeService.deleteByName(name)

    @PostMapping("/apply/{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PASSENGER')")
    fun applyPromoCode(@PathVariable name: String) = promoCodeService.applyPromoCode(name)
}