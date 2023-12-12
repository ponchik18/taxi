package com.modsen.mapper;

import com.modsen.enums.UserRole;
import com.modsen.exception.UserRoleNotFoundException;

public class UserRoleMapper {
    public static UserRole mapToUserRole(String userRole) {
        try{
            return UserRole.valueOf(userRole);
        } catch (IllegalArgumentException exception) {
            throw new UserRoleNotFoundException(userRole);
        }
    }
}