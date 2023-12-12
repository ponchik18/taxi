package com.modsen.service.impl;

import com.modsen.dto.card.CreditCardListResponse;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.card.DefaultCreditCardRequest;
import com.modsen.enums.UserRole;
import com.modsen.exception.CreditCardNotAddException;
import com.modsen.exception.CreditCardNotFoundException;
import com.modsen.exception.DefaultCreditCardNotFoundException;
import com.modsen.exception.UserNotFoundException;
import com.modsen.mapper.CreditCardMapper;
import com.modsen.mapper.UserRoleMapper;
import com.modsen.model.CreditCard;
import com.modsen.model.DefaultCreditCard;
import com.modsen.repository.CreditCardRepository;
import com.modsen.repository.DefaultCreditCardRepository;
import com.modsen.service.CreditCardService;
import com.modsen.service.StripeService;
import com.modsen.service.feigh.DriverServiceClient;
import com.modsen.service.feigh.PassengerServiceClient;
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
    private final DefaultCreditCardRepository defaultCreditCardRepository;
    private final StripeService stripeService;
    private final PassengerServiceClient passengerServiceClient;
    private final DriverServiceClient driverServiceClient;

    @Override
    @Transactional
    public CreditCardResponse addCreditCard(CreditCardRequest creditCardRequest) {
        Customer customer;
        Token token;
        UserRole userRole = UserRoleMapper.mapToUserRole(creditCardRequest.getUserRole());
        DefaultCreditCard defaultCreditCard = defaultCreditCardRepository.findByUserIdAndUserRole(
                creditCardRequest.getUserId(),
                UserRole.valueOf(creditCardRequest.getUserRole())
        ).orElseGet(()-> DefaultCreditCard.builder()
                .userRole(UserRole.valueOf(creditCardRequest.getUserRole()))
                .userId(creditCardRequest.getUserId())
                .build());

        String userEmail = getUserEmailByIdAndUserRole(creditCardRequest.getUserId(), userRole);
        try {
            token = stripeService.createCardToken(creditCardRequest);
            customer = stripeService.getCustomer(defaultCreditCard.getCustomerStripeId(), userEmail);
            stripeService.addSourceToCustomer(customer, token);
        } catch (Exception exception) {
            throw new CreditCardNotAddException(creditCardRequest);
        }

        CreditCard creditCard = CreditCardMapper.MAPPER_INSTANCE.mapToCreditCard(creditCardRequest);
        creditCard.setCustomerId(customer.getId());
        creditCard.setToken(token.getId());
        CreditCard savedCreditCard = creditCardRepository.save(creditCard);

        defaultCreditCard.setCardId(savedCreditCard.getId());
        defaultCreditCard.setCustomerStripeId(customer.getId());
        defaultCreditCardRepository.save(defaultCreditCard);


        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(savedCreditCard);
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
    public CreditCardListResponse getAllUserCreditCard(long userId, String userRole) {
        List<CreditCard> creditCardList = creditCardRepository.findAllByUserIdAndUserRole(userId, UserRoleMapper.mapToUserRole(userRole));
        List<CreditCardResponse> creditCardResponseList = CreditCardMapper.MAPPER_INSTANCE.mapToListOfCreditCardResponse(creditCardList);
        return CreditCardListResponse.builder().creditCardList(creditCardResponseList).totalCountOfCard(creditCardResponseList.size()).build();
    }

    @Override
    public CreditCardResponse makeCreditCardDefault(DefaultCreditCardRequest defaultCreditCardRequest) {
        passengerServiceClient.getPassengerById(defaultCreditCardRequest.getUserId());
        DefaultCreditCard defaultCreditCard = defaultCreditCardRepository.findByUserIdAndUserRole(
                defaultCreditCardRequest.getUserId(),
                UserRoleMapper.mapToUserRole(defaultCreditCardRequest.getUserRole())
        ).orElseThrow(()->new DefaultCreditCardNotFoundException(defaultCreditCardRequest.getUserRole(), defaultCreditCardRequest.getUserId()));

        Long creditCartId = defaultCreditCardRequest.getCardId();
        CreditCard creditCard = creditCardRepository.findById(creditCartId)
                .orElseThrow(()->new CreditCardNotFoundException(creditCartId));
        defaultCreditCard.setCardId(creditCartId);
        defaultCreditCardRepository.save(defaultCreditCard);

        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    @Override
    public CreditCardResponse getDefaultCardForUser(long userId, String userRole) {
        DefaultCreditCard defaultCreditCard = defaultCreditCardRepository.findByUserIdAndUserRole(
                userId, UserRoleMapper.mapToUserRole(userRole)
        ).orElseThrow(()->new UserNotFoundException(userRole, userId));

        Long creditCartId = defaultCreditCard.getCardId();
        CreditCard creditCard = creditCardRepository.findById(creditCartId)
                .orElseThrow(()->new CreditCardNotFoundException(creditCartId));

        return CreditCardMapper.MAPPER_INSTANCE.mapToCreditCardResponse(creditCard);
    }

    private String getUserEmailByIdAndUserRole(Long userId, UserRole userRole) {
        return switch (userRole){
            case DRIVER -> driverServiceClient.getDriverById(userId).email();
            case PASSENGER -> passengerServiceClient.getPassengerById(userId).email();
        };
    }
}