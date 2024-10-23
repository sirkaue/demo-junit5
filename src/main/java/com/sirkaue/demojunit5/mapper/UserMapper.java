package com.sirkaue.demojunit5.mapper;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

public class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserRequestDto dto) {
        return new User(dto.id(), dto.name(), dto.email(), dto.password());
    }

    public static UserResponseDto toUserDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
