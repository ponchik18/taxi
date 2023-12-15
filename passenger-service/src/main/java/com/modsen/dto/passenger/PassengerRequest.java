package com.modsen.dto.passenger;

import com.modsen.constants.PassengerServiceConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequest {
    @NotEmpty(message = PassengerServiceConstants.Validation.Message.FIELD_EMPTY)
    private String firstName;
    @NotEmpty(message = PassengerServiceConstants.Validation.Message.FIELD_EMPTY)
    private String lastName;
    @NotEmpty(message = PassengerServiceConstants.Validation.Message.FIELD_EMPTY)
    @Email(message = PassengerServiceConstants.Validation.Message.EMAIL_FORMAT)
    private String email;
    @NotEmpty(message = PassengerServiceConstants.Validation.Message.FIELD_EMPTY)
    @Pattern(regexp = PassengerServiceConstants.Validation.Format.PHONE_FORMAT, message = PassengerServiceConstants.Validation.Message.PHONE_FORMAT)
    private String phone;
}