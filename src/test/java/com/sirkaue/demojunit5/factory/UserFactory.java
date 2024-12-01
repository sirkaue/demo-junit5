package com.sirkaue.demojunit5.factory;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;

public class UserFactory {

    private UserFactory() {
    }

    public static User createDefaultUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("example@email.com");
        user.setPassword("123456");
        return user;
    }

    public static UserRequestDto createDefaultUserRequestDto() {
        return new UserRequestDto("John", "example@email.com", "123456");
    }

    public static UserResponseDto createDefaultUserResponseDto() {
        return new UserResponseDto(1L, "John", "example@email.com");
    }

    public static UserRequestDto createUpdatedUserRequestDto() {
        return new UserRequestDto("Jane", "UpdatedEmail@email.com", "123456");
    }

    public static Long getExistingId() {
        return 1L;
    }

    public static Long getNonExistingId() {
        return 99L;
    }
}