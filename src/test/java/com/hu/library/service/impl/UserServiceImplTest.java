package com.hu.library.service.impl;

import com.hu.library.enums.UserType;
import com.hu.library.repository.UserRepository;
import com.hu.library.repository.impl.XMLUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new XMLUserRepository();
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testRegister_UserExists() {
        String userName = "testUser";
        userRepository.addUser(UserType.User, userName, "password"); // 先添加用户

        userService.register(UserType.User, userName, "password");

    }

    @Test
    void testRegister_Success() {
        String userName = "testUser";

        userService.register(UserType.User, userName, "password");

    }

    @Test
    void testLogin_UserNotExists() {
        String userName = "testUser";

        userService.login(userName, "password");

    }

    @Test
    void testLogin_IncorrectPassword() {
        String userName = "testUser";
        userRepository.addUser(UserType.User, userName, "correctPassword");

        userService.login(userName, "wrongPassword");

    }

    @Test
    void testLogin_Success() {
        String userName = "testUser";
        userRepository.addUser(UserType.User, userName, "correctPassword");

        userService.login(userName, "correctPassword");

    }
}
