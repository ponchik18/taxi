package com.modsen.dto.card;

import com.modsen.constants.PaymentServiceConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardRequest {
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String cardHolder;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    @Pattern(regexp = PaymentServiceConstants.Validation.Format.CARD_NUMBER_FORMAT, message = PaymentServiceConstants.Validation.Message.NOT_RIGHT_FORMAT)
    private String cardNumber;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String expMonth;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String expYear;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String cvc;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String userRole;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long userId;
}