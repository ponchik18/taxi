package com.modsen.promocodeservice.exception

import com.modsen.promocodeservice.constants.PromoCodeServiceConstant

class PromoCodeNotFountException(val name: String):
    RuntimeException(PromoCodeServiceConstant.Errors.Message.PROMO_CODE_NOT_FOUND.format(name)) {}