package com.modsen.service.impl;

import com.modsen.dto.card.CreditCardListResponse;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.card.DefaultCreditCardRequest;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.CreditCardNotFoundException;
import com.modsen.exception.DefaultCardDeletionException;
import com.modsen.exception.DefaultCreditCardNotFoundException;
import com.modsen.exception.StripeCustomerNotFoundException;
import com.modsen.mapper.CreditCardMapper;
import com.modsen.mapper.UserRoleMapper;
import com.modsen.model.CreditCard;
import com.modsen.model.StripeCustomer;
import com.modsen.repository.CreditCardRepository;
import com.modsen.repository.StripeCustomerRepository;
import com.modsen.service.CreditCardService;
import com.modsen.service.StripeService;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final StripeService stripeService;
    private final PassengerServiceClient passengerServiceClient;
    private final DriverServiceClient driverServiceClient;
    private final StripeCustomerRepository stripeCustomerRepository;

    @Override
    @Transactional
    public CreditCardResponse addCreditCard(CreditCardRequest creditCardRequest) {
        UserRole userRole = UserRoleMapper.mapToUserRole(creditCardRequest.getUserRole());

        String userEmail = getUserEmailByIdAndUserRole(creditCardRequest.getUserId(), userRole);
        StripeCustomer stripeCustomer = getOrCreateStripeCustomer(creditCardRequest.getUserId(), userRole);

        String cardId;
        Customer customer;
        try {
            Token token = stripeService.createCardToken(creditCardRequest);
            customer = stripeService.getCustomer(stripeCustomer.getCustomerId(), userEmail);
            cardId = stripeService.createCardForCustomer(customer.getId(), token);
        } catch (Exception exception) {
            throw new CreditCardNotAddException(creditCardRequest);
        }

        stripeCustomer.setCustomerId(customer.getId());
        stripeCustomerRepository.save(stripeCustomer);

        CreditCard creditCard = CreditCardMapper.MAPPER_INSTANCE.mapToCreditCard(creditCardRequest);
        creditCard.setStripeCardId(cardId);
        CreditCard savedCreditCard = creditCardRepository.save(creditCard);

        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(savedCreditCard);
    }

    private StripeCustomer getOrCreateStripeCustomer(Long userId, UserRole userRole) {
        return stripeCustomerRepository.findByUserIdAndUserRole(userId, userRole)
                .orElseGet(() -> {
                    StripeCustomer newStripeCustomer = new StripeCustomer();
                    newStripeCustomer.setUserId(userId);
                    newStripeCustomer.setUserRole(userRole);
                    return newStripeCustomer;
                });
    }

    @Override
    public void deleteCreditCard(long cardId, long userId, String userRole) {
        CreditCard creditCard = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new CreditCardNotFoundException(cardId));

        StripeCustomer stripeCustomer = getStripeCustomer(userId, UserRoleMapper.mapToUserRole(userRole));
        String customerId = stripeCustomer.getCustomerId();
        try {
            if (stripeService.getDefaultCardIdForCustomer(customerId).equals(creditCard.getStripeCardId())) {
                throw new DefaultCardDeletionException(cardId);
            }
            stripeService.deleteCard(customerId, creditCard.getStripeCardId());

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        creditCardRepository.delete(creditCard);
    }

    @Override
    public CreditCardResponse getCreditCardById(long id) {
        CreditCard creditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new CreditCardNotFoundException(id));
        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    @Override
    public CreditCardListResponse getAllUserCreditCard(long userId, String userRole) {
        List<CreditCard> creditCardList = creditCardRepository.findAllByUserIdAndUserRole(
                userId,
                UserRoleMapper.mapToUserRole(userRole)
        );
        List<CreditCardResponse> creditCardResponseList = CreditCardMapper.MAPPER_INSTANCE.mapToListOfCreditCardResponse(creditCardList);
        return CreditCardListResponse.builder()
                .creditCardList(creditCardResponseList)
                .countOfCard(creditCardResponseList.size())
                .build();
    }

    @Override
    public CreditCardResponse makeCreditCardDefault(DefaultCreditCardRequest defaultCreditCardRequest) {
        Long userId = defaultCreditCardRequest.getUserId();
        UserRole userRole = UserRoleMapper.mapToUserRole(defaultCreditCardRequest.getUserRole());
        Long creditCartId = defaultCreditCardRequest.getCardId();

        StripeCustomer stripeCustomer = getStripeCustomer(userId, userRole);
        CreditCard creditCard = creditCardRepository.findById(creditCartId)
                .orElseThrow(() -> new CreditCardNotFoundException(creditCartId));

        try {
            stripeService.makeCreditCardDefault(stripeCustomer.getCustomerId(), creditCard.getStripeCardId());
        } catch (StripeException e) {
            throw new DefaultCreditCardNotFoundException(userRole.name(), userId);
        }

        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    @Override
    public CreditCardResponse getDefaultCardForUser(long userId, String userRole) {
        StripeCustomer stripeCustomer = getStripeCustomer(userId, UserRoleMapper.mapToUserRole(userRole));

        String stripeCardId;
        try {
            stripeCardId = stripeService.getDefaultCardIdForCustomer(stripeCustomer.getCustomerId());
        } catch (StripeException e) {
            throw new DefaultCreditCardNotFoundException(userRole, userId);
        }
        CreditCard creditCard = creditCardRepository.findByStripeCardId(stripeCardId)
                .orElseThrow(() -> new DefaultCreditCardNotFoundException(userRole, userId));

        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    private StripeCustomer getStripeCustomer(Long userId, UserRole userRole) {
        return stripeCustomerRepository.findByUserIdAndUserRole(userId, userRole)
                .orElseThrow(() -> new StripeCustomerNotFoundException(userId, userRole.name()));
    }

    private String getUserEmailByIdAndUserRole(Long userId, UserRole userRole) {
        return switch (userRole) {
            case DRIVER -> driverServiceClient.getDriverById(userId).email();
            case PASSENGER -> passengerServiceClient.getPassengerById(userId).email();
        };
    }
}