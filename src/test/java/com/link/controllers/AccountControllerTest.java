package com.link.controllers;

import com.link.service.JWTServiceImpl;
import com.link.service.UserServiceImpl;
import com.link.util.PasswordAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountControllerTest {

    private UserServiceImpl userService;
    private JWTServiceImpl jwtService;
    private PasswordAuthentication authorizer;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
        jwtService = new JWTServiceImpl();
        authorizer = new PasswordAuthentication();
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