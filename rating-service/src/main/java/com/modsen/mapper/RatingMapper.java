package com.modsen.mapper;

import com.modsen.dto.RatingRequest;
import com.modsen.dto.RatingResponse;
import com.modsen.enums.UserRole;
import com.modsen.exception.UserRoleNotFoundException;
import com.modsen.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RatingMapper {
    RatingMapper MAPPER_INSTANCE = Mappers.getMapper(RatingMapper.class);

    Rating mapToRating(RatingRequest ratingRequest);

    RatingResponse mapToRatingResponse(Rating rating);

    List<RatingResponse> mapToListOfRatingResponse(List<Rating> ratings);

    default UserRole mapStringToUserRole(String userRole) {
        try {
            return UserRole.valueOf(userRole);
        } catch (IllegalArgumentException exception) {
            throw new UserRoleNotFoundException(userRole);
        }
    }

    default String mapUserRoleToString(UserRole userRole) {
        return userRole.name();
    }

}