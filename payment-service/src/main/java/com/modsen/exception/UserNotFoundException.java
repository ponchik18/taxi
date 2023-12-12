package com.modsen.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userRole, long userId) {
        super(String.format("User with id '%d' and role '%s' not exist!", userId,userRole));
    }
}