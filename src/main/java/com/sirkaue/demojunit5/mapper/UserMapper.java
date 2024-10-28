package com.sirkaue.demojunit5.mapper;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

import java.util.List;

public interface UserMapper {
    User toUser(UserRequestDto dto);

    UserResponseDto toUserDto(User user);

    List<UserResponseDto> toUserDto(List<User> users);
}
