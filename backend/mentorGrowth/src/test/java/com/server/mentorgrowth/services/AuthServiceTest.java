package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void saveUser_withValidFields_shouldReturnUserTest() {
        when(authService.saveUser(any(RegisterRequest.class))).thenReturn(UserAuthResponse.builder()
                        .id("dbc5a8f1-1a4a-4d0f-9f82-d8b48eb9dafc")
                        .token("Your token was created successfully")
                        .build());

        RegisterRequest request = RegisterRequest.builder()
                .firstName("Emma")
                .lastName("Adah")
                .role("ROLE_MENTEE")
                .email("emma007@gmail.com")
                .password("adah007")
                .build();
        UserAuthResponse response = authService.saveUser(request);
        assertNotNull(response);
    }

    @Test
    void login() {
    }
}