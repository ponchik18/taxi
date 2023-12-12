package com.modsen.exception;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String userRole) {
        super(String.format("UserRole '%s' not exist!", userRole));
    }
}