package com.modsen.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreditCardListResponse (
        List<CreditCardResponse> creditCardList,
        int totalCountOfCard
) {
}