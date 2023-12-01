package com.modsen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardRequest {
    private String cardHolder;
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String userRole;
    private Long userId;
}