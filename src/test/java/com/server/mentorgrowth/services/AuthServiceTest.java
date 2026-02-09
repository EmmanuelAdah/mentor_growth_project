package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.LoginRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.exceptions.InvalidCredentialsException;
import com.server.mentorgrowth.exceptions.UserAlreadyExistException;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")));
        user.setRole(Role.ROLE_MENTOR);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");
    }

    @Test
    public void saveUser_shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Adah");
        request.setLastName("Smith");
        request.setRole("MENTOR");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        UserAuthResponse response = authService.saveUser(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e", response.id());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_shouldThrowExceptionWhenUserExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(
                UserAlreadyExistException.class,
                () -> authService.saveUser(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldAuthenticateSuccessfully() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userService.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        UserAuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e", response.id());
    }

    @Test
    void login_shouldThrowExceptionForInvalidUserCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong-password");

        when(userService.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void googleLogin_shouldCreateUserAndReturnResponse() {
        User savedUser = new User();
        savedUser.setId("25e6f3e4-8afb-4469-8917-fedbdb5bad18");
        savedUser.setEmail("google@example.com");

        UserAuthResponse expectedResponse = UserAuthResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(modelMapper.map(savedUser, UserAuthResponse.class))
                .thenReturn(expectedResponse);

        UserAuthResponse response =
                authService.googleLogin("google@example.com", "John Doe");

        assertNotNull(response);
        assertEquals("25e6f3e4-8afb-4469-8917-fedbdb5bad18", response.id());
        assertEquals("google@example.com", response.email());

        verify(userRepository).save(any(User.class));
    }
}
