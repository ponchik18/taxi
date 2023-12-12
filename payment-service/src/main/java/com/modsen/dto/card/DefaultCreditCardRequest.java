package com.modsen.dto.card;

import com.modsen.constants.PaymentServiceConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultCreditCardRequest {
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long userId;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private String userRole;
    @NotNull(message = PaymentServiceConstants.Validation.Message.FIELD_EMPTY)
    private Long cardId;
}