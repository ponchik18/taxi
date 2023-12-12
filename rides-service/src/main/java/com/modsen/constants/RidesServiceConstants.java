package com.modsen.constants;

public interface RidesServiceConstants {
    interface Validation {
        interface Message {
            String PASSENGER_ID_NOT_MIN = "Passenger id should be more than 1";
            String PICK_UP_LOCATION_EMPTY = "Pick up location is mandatory";
            String DROP_LOCATION_EMPTY = "Drop location is mandatory";
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
    }
}