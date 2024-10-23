package com.sirkaue.demojunit5.service;

import com.sirkaue.demojunit5.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto findById(Long id);
}
