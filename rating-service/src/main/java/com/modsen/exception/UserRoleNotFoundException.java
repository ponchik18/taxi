package com.modsen.exception;

import lombok.Getter;

@Getter
public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String userRole) {
        super("UserRole '"+userRole+"' not found!");
    }
}