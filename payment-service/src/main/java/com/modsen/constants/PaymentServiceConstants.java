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
        }
    }
    interface BasePath {
        String PAYMENT_CONTROLLER_PATH = "/api/v1/payment";
        String CARD_CONTROLLER_PATH = PAYMENT_CONTROLLER_PATH + "/card";
    }
}