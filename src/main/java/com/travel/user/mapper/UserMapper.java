package com.travel.user.mapper;

import com.travel.user.dto.UserResponse;
import com.travel.user.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
