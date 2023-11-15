package com.modsen.constants;

public interface RidesServiceConstants {
    interface Validation {
        interface Message {
            String DRIVER_ID_NOT_MIN = "Driver id should be more than 1";
            String PASSENGER_ID_NOT_MIN = "Passenger id should be more than 1";
            String PICK_UP_LOCATION_EMPTY = "Pick up location is mandatory";
            String DROP_LOCATION_EMPTY = "Drop location is mandatory";
            String START_TIME_EMPTY = "Start time is mandatory";
            String END_TIME_EMPTY = "End time is mandatory";
            String STATUS_EMPTY = "Status is mandatory";
            String COST_MORE_THAN_ZERO = "Cost must be greater than or equal to 0";
            String END_TIME_IS_BEFORE_AFTER = "End time must be after start time";
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