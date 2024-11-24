package com.sirkaue.demojunit5.service.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.exception.EmailUniqueViolationException;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.mapper.UserMapper;
import com.sirkaue.demojunit5.repository.UserRepository;
import com.sirkaue.demojunit5.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDto(users);
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailUniqueViolationException("Email already exists");
        }

        User user = userMapper.toUser(dto);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void update(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("User not found with id: " + id));

        String userEmail = user.getEmail();
        if (!userEmail.equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new EmailUniqueViolationException("Email is already in use by another user.");
        }

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
