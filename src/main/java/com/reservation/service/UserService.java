package com.reservation.service;

import com.reservation.domain.User;
import com.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Class for managing user-related logic
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(User user) {
        return userRepository.login(user);
    }
}
