package com.modsen.constants;

public interface DriverServiceConstants {
    interface Validation {
        interface Message {
            String LICENSE_NUMBER_EMPTY = "License number is mandatory";
            String FIRST_NAME_EMPTY = "Firstname is mandatory";
            String LASTNAME_EMPTY = "Lastname is mandatory";
            String EMAIL_EMPTY = "Email is mandatory";
            String EMAIL_FORMAT = "Not the right format for email";
            String PHONE_EMPTY = "Phone is mandatory";
            String PHONE_FORMAT = "Not the right format for phone";
        }

        interface Format {
            String PHONE_FORMAT = "(?:\\+\\d{1,2}\\s?)?(\\d{3}[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}";
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
            String DRIVER_NOT_FOUND = "Driver with id %d not found!";
            String NOT_VALID_FIELD = "There is an error in the information entered in the driver's fields!";
            String DUPLICATE_DRIVER_WITH_EMAIL = "Driver with email '%s' has already defined";
            String DUPLICATE_DRIVER_WITH_PHONE = "Driver with phone '%s' has already defined";
            String DUPLICATE_DRIVER_WITH_LICENSE_NUMBER = "Driver with license number '%s' has already defined";
        }
    }
    interface Path {
        String DRIVER_CONTROLLER_PATH = "/api/v1/driver";
    }
}