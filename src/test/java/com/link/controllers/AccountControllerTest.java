package com.link.controllers;

import com.link.service.JWTServiceImpl;
import com.link.service.UserServiceImpl;
import com.link.util.PasswordAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountControllerTest {

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountController = new AccountController();
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