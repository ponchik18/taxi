package com.modsen.constants;

public interface DriverServiceTestConstants {
    interface TestData {
        String EMAIL = "passenger@mail.com";
        String PHONE = "+375111111111";
        String LICENSE_NUMBER = "lic_465871879458798";
    }
    interface KafkaProperties {
        String TRUSTED_PACKAGE_KEY = "spring.json.trusted.packages";
        String TRUSTED_PACKAGE_VALUE = "com.modsen.dto.driver";
    }
}