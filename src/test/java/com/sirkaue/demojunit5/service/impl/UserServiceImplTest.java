package com.sirkaue.demojunit5.service.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.exception.EmailUniqueViolationException;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.factory.UserFactory;
import com.sirkaue.demojunit5.mapper.UserMapper;
import com.sirkaue.demojunit5.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private Long existingId;
    private Long nonExistingId;
    private String updatedEmail;

    @BeforeEach
    void setUp() {
        user = UserFactory.createDefaultUser();
        userRequestDto = UserFactory.createDefaultUserRequestDto();
        userResponseDto = UserFactory.createDefaultUserResponseDto();
        userRequestDto = UserFactory.createUpdatedUserRequestDto();
        existingId = UserFactory.getExistingId();
        nonExistingId = UserFactory.getNonExistingId();
        updatedEmail = UserFactory.createUpdatedUserRequestDto().email();
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

    @Test
    void shouldCreateWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userMapper.toUser(Mockito.any())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = userService.create(new UserRequestDto(user.getName(), user.getEmail(), user.getPassword()));

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());

        verify(userMapper, times(1)).toUser(Mockito.any());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void shouldNotCreateAndThrowEmailUniqueViolationExceptionWhenEmailIsAlreadyInUse() {
        // Arrange
        final String EXPECTED_MESSAGE = "Email already exists: " + user.getEmail();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act
        Executable executable = () -> userService.create(new UserRequestDto(user.getName(), user.getEmail(), user.getPassword()));

        // Assert
        var exception = assertThrows(EmailUniqueViolationException.class, executable);

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
        assertEquals(user.getId(), userResponseDto.id());
        assertEquals(user.getName(), userResponseDto.name());
        assertEquals(user.getEmail(), userResponseDto.email());

        verify(userRepository, Mockito.never()).save(user);
        verify(userMapper, Mockito.never()).toUserDto(user);
    }

    @Test
    void shouldThrowEmailUniqueViolationExceptionWhenEmailExists() {
        // Arrange
        final String EXPECTED_MESSAGE = "Email already exists: " + user.getEmail();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act
        Executable executable = () -> userService.create(new UserRequestDto(user.getName(), user.getEmail(), user.getPassword()));

        // Assert
        var exception = assertThrows(EmailUniqueViolationException.class, executable);
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userMapper, never()).toUser(any(UserRequestDto.class));
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toUserDto(any(User.class));
    }

    @Test
    void shouldUpdateWhenUserExists() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(userRequestDto.email())).thenReturn(false);

        // Act
        userService.update(user.getId(), userRequestDto);

        // Assert
        assertEquals(userRequestDto.name(), user.getName());
        assertEquals(userRequestDto.email(), user.getEmail());
        assertEquals(userRequestDto.password(), user.getPassword());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).existsByEmail(userRequestDto.email());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldNotUpdateAndThrowObjectNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        final String EXPECTED_MESSAGE = "User not found with id: " + nonExistingId;

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.update(nonExistingId, new UserRequestDto(user.getName(), user.getEmail(), user.getPassword())));

        // Assert
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
        assertEquals(user.getId(), userResponseDto.id());
        assertEquals(user.getName(), userResponseDto.name());
        assertEquals(user.getEmail(), userResponseDto.email());

        verify(userRepository, Mockito.times(1)).findById(nonExistingId);
        verify(userRepository, Mockito.never()).existsByEmail(user.getEmail());
        verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void shouldNotUpdateAndThrowEmailUniqueViolationExceptionWhenEmailIsAlreadyInUse() {
        // Arrange
        final String EXPECTED_MESSAGE = "Email already exists: " + updatedEmail;

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updatedEmail)).thenReturn(true);

        // Act
        var exception = assertThrows(EmailUniqueViolationException.class,
                () -> userService.update(user.getId(), userRequestDto));

        // Assert
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
        assertEquals(user.getId(), userResponseDto.id());
        assertEquals(user.getName(), userResponseDto.name());
        assertEquals(user.getEmail(), userResponseDto.email());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).existsByEmail(updatedEmail);
        verify(userRepository, never()).save(user);
    }

    @Test
    void shouldDeleteWhenUserExists() {
        // Arrange
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        // Act
        userService.delete(existingId);

        // Assert
        verify(userRepository, Mockito.times(1)).findById(existingId);
        verify(userRepository, Mockito.times(1)).delete(user);
        verify(userRepository, Mockito.never()).deleteById(nonExistingId);
    }

    @Test
    void shouldNotDeleteAndThrowObjectNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        final String EXPECTED_MESSAGE = "User not found with id: " + nonExistingId;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Executable executable = () -> userService.delete(nonExistingId);

        // Assert
        Exception exception = assertThrows(ObjectNotFoundException.class, executable);
        assertEquals(EXPECTED_MESSAGE, exception.getMessage());

        verify(userRepository, Mockito.times(1)).findById(nonExistingId);
        verify(userRepository, Mockito.never()).delete(user);
        verify(userRepository, Mockito.never()).deleteById(nonExistingId);
    }
}
