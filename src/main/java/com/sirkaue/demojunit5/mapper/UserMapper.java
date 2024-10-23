package com.sirkaue.demojunit5.mapper;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

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


    public static List<UserResponseDto> toUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
