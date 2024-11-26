package com.sirkaue.demojunit5.mapper.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    @Override
    public UserResponseDto toUserDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public List<UserResponseDto> toUserDto(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}
