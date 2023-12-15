package com.modsen.mapper;

import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.model.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CreditCardMapper {
    CreditCardMapper MAPPER_INSTANCE = Mappers.getMapper(CreditCardMapper.class);

    CreditCardResponse mapToCreditCardResponse(CreditCard creditCard);

    List<CreditCardResponse> mapToListOfCreditCardResponse(List<CreditCard> creditCards);

    CreditCard mapToCreditCard(CreditCardRequest creditCardRequest);
}