package com.sirkaue.demojunit5.mapper;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

public class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    public static UserResponseDto toUserDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
