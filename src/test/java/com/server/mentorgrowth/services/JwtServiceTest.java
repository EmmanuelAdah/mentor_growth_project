package com.server.mentorgrowth.services;

import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService = new JwtService();
    private final User user = new User();

    @BeforeEach
    void setUp() {
        String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(
                "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
        ReflectionTestUtils.setField(jwtService, "accessTokenTtlMillis", 43_200_000L);
        user.setId("user-123");
        user.setEmail("person@example.com");
        user.setRole(Role.ROLE_MENTEE);
    }

    @Test
    void generatedTokenContainsTheAuthenticatedUserId() {
        String token = jwtService.generateToken(user);

        assertEquals("user-123", jwtService.extractUserId(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void tokenCannotAuthenticateAnotherUserWithTheSameEmail() {
        String token = jwtService.generateToken(user);
        User anotherUser = new User();
        anotherUser.setId("user-456");
        anotherUser.setEmail(user.getEmail());
        anotherUser.setRole(Role.ROLE_MENTEE);

        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }
}
