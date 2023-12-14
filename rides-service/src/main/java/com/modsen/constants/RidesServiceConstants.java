package com.modsen.constants;

public interface RidesServiceConstants {
    interface Validation {
        interface Message {
            String FIELD_EMPTY = "Field is mandatory";
            String MARK_NOT_RIGHT_RANGE = "Mark should be equal or more than '1' and equal or less than '5'";
        }
    }
    interface DefaultValue {
        int PAGE = 0;
        int ELEMENT_PER_PAGE = 10;
        String SORT_ASC = "asc";
        String SORT_DESC = "dsc";
    }
    interface Errors {
        interface Message {
            String RIDE_NOT_FOUND = "Ride with id %d not found!";
            String NOT_VALID_FIELD = "There is an error in the information entered in the ride's fields!";
        }
    }
    interface Path {
        String RIDES_CONTROLLER_PATH = "/api/v1/rides";
        String PASSENGER_SERVICE_PATH = "http://passenger-service/api/v1/passenger";
        String PROMO_CODE_SERVICE_PATH = "http://promo-code-service/api/v1/promo-code";
        String PAYMENT_SERVICE_PATH = "http://payment-service/api/v1/payment";
    }
    interface KafkaProperties {
        String AUTO_OFFSET_RESET_CONFIG_VALUE = "earliest";
        String TRUSTED_PACKAGE_KEY = "spring.json.trusted.packages";
        String TRUSTED_PACKAGE_VALUE = "com.modsen.dto.rating,com.modsen.dto.card,com.modsen.dto.promo,com.modsen.dto.rides,com.modsen.dto.payment,com.modsen.dto.driver";
        String MESSAGE_KEY_EXPRESSION = "kafka-integration";
    }
}