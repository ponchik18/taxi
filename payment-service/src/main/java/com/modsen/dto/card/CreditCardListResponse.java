package com.modsen.dto.card;

import lombok.Builder;

import java.util.List;

@Builder
public record CreditCardListResponse (
        List<CreditCardResponse> creditCardList,
        int countOfCard
) {
}