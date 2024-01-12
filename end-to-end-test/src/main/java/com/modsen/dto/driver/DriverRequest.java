package com.modsen.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    private String licenseNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String driverStatus;
}