package com.modsen.constants;

public interface RatingServiceConstants {
    interface Validation {
        interface Message {
            String ENTITY_ID_EMPTY = "Entity id is mandatory and should be more than 0";
            String MARK_EMPTY = "Mark is mandatory";
            String MARK_NOT_RIGHT_RANGE = "Mark should be equal or more than '1' and equal or less than '5'";
            String USER_ROLE_EMPTY = "UserRole is mandatory";
        }
    }
    interface Errors {
        interface Message {
            String RATING_NOT_FOUND = "Rating with id %d not found!";
            String NOT_VALID_FIELD = "There is an error in the information entered in the rating's fields!";
        }
    }
    interface Path {
        String RATING_CONTROLLER_PATH = "/api/v1/rating";
    }
}