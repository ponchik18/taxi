package com.modsen.constants;

public interface PaymentServiceTestConstants {
    interface TestData {
        String CARD_NUMBER = "4242424242424242";
        String CARD_HOLDER = "Test Test";
        Long PASSENGER_ID = 4L;
        Long DRIVER_ID = 5L;
        String STRIPE_CARD_ID_2 = "card_0000000";
        String EXP_MONTH = "10";
        String EXP_YEAR = "26";
        String CVC = "111";
        String STRIPE_CARD_ID_1 = "card_1111111";
        String CUSTOMER_ID = "cus_123456789";
        String EMAIL = "test@test.com";
        Long CARD_ID = 5L;
        Long RIDE_ID = 9L;
        String EMPTY_STRING = "";
        Integer DRIVER_SERVICE_PORT = 9999;
        Integer PASSENGER_SERVICE_PORT = 9998;
    }
}