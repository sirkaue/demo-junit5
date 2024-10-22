package com.sirkaue.demojunit5.service.impl;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.repository.UserRepository;
import com.sirkaue.demojunit5.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }
}
