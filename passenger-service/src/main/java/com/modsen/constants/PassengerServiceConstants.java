package com.modsen.constants;

public interface PassengerServiceConstants {
    interface Validation {
        interface Message {
            String FIRST_NAME_EMPTY = "Firstname is mandatory";
            String LAST_NAME_EMPTY = "Lastname is mandatory";
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
            String PASSENGER_NOT_FOUND = "Passenger with id %d not found!";
            String NOT_VALID_FIELD = "Passenger in filled fields!";
            String DUPLICATE_PASSENGER_WITH_EMAIL = "Passenger with email '%s' has already defined";
            String DUPLICATE_PASSENGER_WITH_PHONE = "Passenger with phone '%s' has already defined";
        }
    }

    interface Path {
        String PASSENGER_CONTROLLER_PATH = "/api/v1/passenger";
    }
}