package com.link.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTServiceImplTest {

    @Autowired
    JWTServiceImpl jwtService;

    @BeforeEach
    void setUp() {
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
        // Valid token
        String testToken = jwtService.generateToken("Test");
        assertTrue(jwtService.checkToken(testToken));

        // Invalid token
        testToken+="ahhh";
        assertFalse(jwtService.checkToken(testToken));

        // Randomly generated JWT
        String randomToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.Zqe4THpO8zZwF7S4QlNVU9beHqV25V8HDIqZVQjLxkQ";
        assertFalse(jwtService.checkToken(randomToken));
    }
}