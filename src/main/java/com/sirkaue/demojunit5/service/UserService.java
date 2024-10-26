package com.sirkaue.demojunit5.service;

import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto findById(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto create(UserRequestDto dto);

    void update(Long id, UserRequestDto dto);
}
