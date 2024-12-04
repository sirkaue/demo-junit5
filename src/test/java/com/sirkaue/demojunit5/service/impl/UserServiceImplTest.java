package com.sirkaue.demojunit5.service.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.factory.UserFactory;
import com.sirkaue.demojunit5.mapper.UserMapper;
import com.sirkaue.demojunit5.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        user = UserFactory.createDefaultUser();
        userResponseDto = UserFactory.createDefaultUserResponseDto();
        existingId = UserFactory.getExistingId();
        nonExistingId = UserFactory.getNonExistingId();
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

    @Test
    void shouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        final String EXPECTED_MESSAGE = "User not found with id: " + nonExistingId;

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Executable executable = () -> userService.findById(nonExistingId);

        // Assert
        var exception = assertThrows(ObjectNotFoundException.class, executable);
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
        assertEquals(user.getId(), userResponseDto.id());
        assertEquals(user.getName(), userResponseDto.name());
        assertEquals(user.getEmail(), userResponseDto.email());

        verify(userRepository, times(1)).findById(nonExistingId);
        verify(userMapper, Mockito.never()).toUserDto(user);
    }

    @Test
    void shouldFindAllWhenUsersExist() {
        // Arrange
        final int EXPECTED_USER_COUNT = 1;

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserDto(List.of(user))).thenReturn(List.of(userResponseDto));

        // Act
        List<UserResponseDto> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(EXPECTED_USER_COUNT, result.size());
        assertEquals(user.getId(), result.get(0).id());
        assertEquals(user.getName(), result.get(0).name());
        assertEquals(user.getEmail(), result.get(0).email());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toUserDto(List.of(user));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());
        when(userMapper.toUserDto(List.of())).thenReturn(List.of());

        // Act
        List<UserResponseDto> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toUserDto(List.of());
    }
}
