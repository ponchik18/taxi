package com.modsen.dto.driver;

import com.modsen.constants.DriverServiceConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    private String licenseNumber;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    private String firstName;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    private String lastName;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    @Email(message = DriverServiceConstants.Validation.Message.EMAIL_FORMAT)
    private String email;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    @Pattern(regexp = DriverServiceConstants.Validation.Format.PHONE_FORMAT, message = DriverServiceConstants.Validation.Message.PHONE_FORMAT)
    private String phone;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIELD_EMPTY)
    private String driverStatus;
}