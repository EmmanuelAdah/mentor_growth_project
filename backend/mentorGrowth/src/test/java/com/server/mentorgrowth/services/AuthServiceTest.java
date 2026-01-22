package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @MockitoBean
    private AuthService authService;

    @Test
    void saveUser_withValidFields_shouldReturnUserTest() {
//        when(authService.saveUser(any(RegisterRequest.class))).thenReturn(UserAuthResponse.class);

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