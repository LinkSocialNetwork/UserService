package com.link.controllers;

import com.link.service.JWTServiceImpl;
import com.link.service.UserServiceImpl;
import com.link.util.PasswordAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private PasswordAuthentication authorizer;

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        //userService = new UserServiceImpl();
        jwtService = new JWTServiceImpl();
        authorizer = new PasswordAuthentication();
=======
>>>>>>> 817189b2679a766bfc0ad1e3eb3eb628aa94cd04
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insertNewUser() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void checkToken() {
    }
}