package com.modsen.constants;

import java.math.BigDecimal;

public interface PaymentServiceConstants {
    interface Validation {
        interface Message {
            String FIELD_EMPTY = "Field is mandatory";
            String NOT_RIGHT_FORMAT = "Not right format for field";
            String MORE_THAN_ZERO = "Value should be more than '0.00'";
        }

        interface Format {
            String CARD_NUMBER_FORMAT = "\\d{16}";
        }
    }

    interface DefaultValue {
        BigDecimal DRIVE_PERCENTAGE_OF_THE_RIDE = new BigDecimal("0.2");
        int PAGE = 0;
        int ELEMENT_PER_PAGE = 10;
        String SORT_ASC = "asc";
        String SORT_DESC = "dsc";
        String MIN_AMOUNT = "0.01";
    }

    interface Errors {
        interface Message {
            String NOT_VALID_FIELD = "There is an error in the information entered in the fields!";
            String DEFAULT_CREDIT_CARD_DELETION = "Default card with id '%d' can't be deleted";
            String CREDIT_CARD_NOT_ADD = "Card with number '%s' not add!";
            String STRIPE_CUSTOMER_NOT_FOUND = "User with id '%d' and role '%s' not add card!";
            String DEFAULT_CREDIT_CARD_NOT_FOUND = "User with id '%d' and role '%s' hasn't default card!";
            String DRIVER_BALANCE_NOT_FOUNT = "Driver balance with id = '%d' not perform any ride!";
            String NOT_ENOUGH_MONEY = "Not right amount for payout! Actual balance is '%f'. But you enter '%f'";
            String PAYOUT_NOT_PROCESS = "Payout not processed!";
            String RIDE_NOT_PAID = "Error! Ride not paid!";
            String USER_ROLE_NOT_FOUND = "UserRole '%s' not exist!";
        }
    }

    interface BasePath {
        String PAYMENT_CONTROLLER_PATH = "/api/v1/payment";
        String CARD_CONTROLLER_PATH = PAYMENT_CONTROLLER_PATH + "/card";
        String DRIVER_SERVICE_NAME = "driver-service";
        String DRIVER_SERVICE_PATH = "/api/v1/driver";
        String PASSENGER_SERVICE_NAME = "passenger-service";
        String PASSENGER_SERVICE_PATH = "/api/v1/passenger";
    }
}