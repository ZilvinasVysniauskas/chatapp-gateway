package com.chatapp.gateway.mapper;


import com.chatapp.gateway.dto.user.UserResponse;
import com.chatapp.gateway.model.user.User;

public class UserMapper {

    public static UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }
}