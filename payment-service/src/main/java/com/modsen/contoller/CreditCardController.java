package com.modsen.contoller;

import com.modsen.constants.PaymentServiceConstants;
import com.modsen.dto.card.DefaultCreditCardRequest;
import com.modsen.dto.card.CreditCardListResponse;
import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PaymentServiceConstants.BasePath.CARD_CONTROLLER_PATH)
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardResponse addCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.addCreditCard(creditCardRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CreditCardListResponse getAllUserCreditCard(@RequestParam long userId, @RequestParam String userRole) {
        return creditCardService.getAllUserCreditCard(userId, userRole);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCardResponse getCreditCardById(@PathVariable long id) {
        return creditCardService.getCreditCardById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(@PathVariable long id) {
        creditCardService.deleteCreditCard(id);
    }

    @PutMapping("/make-default")
    @ResponseStatus(HttpStatus.OK)
    public CreditCardResponse makeCreditCardDefault(@Valid @RequestBody DefaultCreditCardRequest defaultCreditCardRequest) {
        return creditCardService.makeCreditCardDefault(defaultCreditCardRequest);
    }

    @GetMapping("/give-default")
    @ResponseStatus(HttpStatus.OK)
    public CreditCardResponse gaveDefaultCardForUser(@RequestParam long userId, @RequestParam String userRole) {
        return creditCardService.getDefaultCardForUser(userId, userRole);
    }
}