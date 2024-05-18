package com.reservation.controller;

import com.reservation.domain.User;
import com.reservation.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(UserControllerTest.class);
    }

    @Test
    void newUserTest() {
        User user = new User("name","code");
        Mockito.when(userService.login(user))
                .thenReturn(user);

        User response = userController.newUser(user).getBody();

        Mockito.verify(this.userService, Mockito.times(1)).login(user);
        Mockito.verify(this.userService, Mockito.times(0)).login(null);
        assert (Objects.requireNonNull(response).equals(user));
    }


}
