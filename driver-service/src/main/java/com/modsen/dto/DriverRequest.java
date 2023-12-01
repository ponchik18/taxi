package com.modsen.dto;

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
    @NotEmpty(message = DriverServiceConstants.Validation.Message.LICENSE_NUMBER_EMPTY)
    private String licenseNumber;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.FIRST_NAME_EMPTY)
    private String firstName;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.LASTNAME_EMPTY)
    private String lastName;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.EMAIL_EMPTY)
    @Email(message = DriverServiceConstants.Validation.Message.EMAIL_FORMAT)
    private String email;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.PHONE_EMPTY)
    @Pattern(regexp = DriverServiceConstants.Validation.Format.PHONE_FORMAT, message = DriverServiceConstants.Validation.Message.PHONE_FORMAT)
    private String phone;
    @NotEmpty(message = DriverServiceConstants.Validation.Message.DRIVER_STATUS_EMPTY)
    private String driverStatus;
}