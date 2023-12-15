package com.modsen.constants;

public interface RatingServiceConstants {

    interface Validation {
        interface Message {
            String FIELD_EMPTY = "Field is mandatory";
            String MARK_NOT_RIGHT_RANGE = "Mark should be equal or more than '1' and equal or less than '5'";
        }
    }
    interface Errors {
        interface Message {
            String RATING_ALREADY_CREATED = "Rating for user('%s') with rent id ='%d'";
            String USER_ROLE_NOT_FOUND = "UserRole '%s' not found!";
            String RATING_NOT_FOUND = "Rating with id %d not found!";
            String NOT_VALID_FIELD = "There is an error in the information entered in the rating's fields!";
        }
    }
    interface Path {
        String RATING_CONTROLLER_PATH = "/api/v1/rating";
        String DRIVER_SERVICE_NAME = "driver-service";
        String DRIVER_SERVICE_PATH = "/api/v1/driver";
        String PASSENGER_SERVICE_NAME = "passenger-service";
        String PASSENGER_SERVICE_PATH = "/api/v1/passenger";
        String RIDES_SERVICE_NAME = "rides-service";
        String RIDES_SERVICE_PATH = "/api/v1/rides";
    }
}