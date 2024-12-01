package com.sirkaue.demojunit5.service.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.factory.UserFactory;
import com.sirkaue.demojunit5.mapper.UserMapper;
import com.sirkaue.demojunit5.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto userResponseDto;
    private Long existingId;

    @BeforeEach
    void setUp() {
        user = UserFactory.createDefaultUser();
        userResponseDto = UserFactory.createDefaultUserResponseDto();
        existingId = UserFactory.getExistingId();
    }

    @Test
    void shouldFindByIdWhenIdExists() {
        // Arrange
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = userService.findById(existingId);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());

        verify(userRepository, times(1)).findById(existingId);
        verify(userMapper, times(1)).toUserDto(user);
    }
}
