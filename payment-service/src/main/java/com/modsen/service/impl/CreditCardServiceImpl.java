package com.modsen.service.impl;

import com.modsen.dto.CreditCardListResponse;
import com.modsen.dto.CreditCardRequest;
import com.modsen.dto.CreditCardResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotFoundException;
import com.modsen.mapper.CreditCardMapper;
import com.modsen.model.CreditCard;
import com.modsen.repository.CreditCardRepository;
import com.modsen.service.CreditCardService;
import com.modsen.service.StripeService;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final StripeService stripeService;

    @Override
    public CreditCardResponse addCreditCard(CreditCardRequest creditCardRequest) {
        Token token = stripeService.createCardToken(creditCardRequest);
        CreditCard creditCard = CreditCardMapper.MAPPER_INSTANCE.mapToCreditCard(creditCardRequest);
        creditCard.setToken(token.getId());
        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCardRepository.save(creditCard));
    }

    @Override
    public void deleteCreditCard(long id) {
        if (creditCardRepository.existsById(id)) {
            creditCardRepository.deleteById(id);
        }
    }

    @Override
    public CreditCardResponse getCreditCardById(long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> new CreditCardNotFoundException(id));
        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    @Override
    public CreditCardListResponse getAllUserCreditCard(long userId, UserRole userRole) {
        List<CreditCard> creditCardList = creditCardRepository.findAllByUserIdAndUserRole(userId, userRole);
        List<CreditCardResponse> creditCardResponseList = CreditCardMapper.MAPPER_INSTANCE.mapToListOfCreditCardResponse(creditCardList);
        return CreditCardListResponse.builder().creditCardList(creditCardResponseList).totalCountOfCard(creditCardResponseList.size()).build();
    }
}