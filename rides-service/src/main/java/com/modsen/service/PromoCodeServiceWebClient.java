package com.modsen.service;


import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;

import java.util.Optional;

public interface PromoCodeServiceWebClient {
    Optional<PromoCodeResponse> applyPromoCodeForRide(PromoCodeApplyRequest promoCodeApplyRequest);
}