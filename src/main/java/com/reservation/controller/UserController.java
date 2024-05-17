package com.reservation.controller;

import com.reservation.domain.User;
import com.reservation.domain.response.UserResponse;
import com.reservation.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users/login")
    public UserResponse newUser(@RequestBody User user) {
        return new UserResponse(userService.login(user), null);
    }
}