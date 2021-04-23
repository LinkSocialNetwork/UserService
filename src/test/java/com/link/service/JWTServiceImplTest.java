package com.link.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceImplTest {

    JWTServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTServiceImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateToken() {
        // Token against token
        assertEquals(jwtService.generateToken("testAccount"), jwtService.generateToken("testAccount"));

        // Token against different token
        assertNotEquals(jwtService.generateToken("testAccount"), jwtService.generateToken("other"));
    }

    @Test
    void checkToken() {
        String testToken = jwtService.generateToken("Test");
        assertTrue(jwtService.checkToken(testToken));
    }
}