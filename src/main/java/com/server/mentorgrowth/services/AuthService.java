package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.LoginRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.exceptions.InvalidCredentialsException;
import com.server.mentorgrowth.exceptions.UserAlreadyExistException;
import com.server.mentorgrowth.models.Mentee;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.UUID;
import static com.server.mentorgrowth.utils.Mapper.map;
import static com.server.mentorgrowth.utils.Mapper.validateFields;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserAuthResponse saveUser(RegisterRequest request) {
        validateFields(request);

        request.setPassword(Objects.requireNonNull(passwordEncoder.encode(request.getPassword())));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException("User already exists");
        }
        User user = userRepository.save(map(request));
        return response(user, jwtService.generateToken(user));
    }

    public UserAuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return response(user, token);
    }

    private UserAuthResponse response(User user, String token) {
        return UserAuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name()
                        .split("_")[1]
                        .toLowerCase())
                .token(token)
                .build();
    }

    public UserAuthResponse googleLogin(String email, String name) {
        String[] names = Objects.requireNonNull(name).trim().split("\\s+", 2);
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";
        log.info("Google login from email: {} and name: {} {}", email, firstName, lastName);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Mentee mentee = new Mentee();
            mentee.setEmail(email.trim().toLowerCase());
            mentee.setFirstName(firstName.toUpperCase());
            mentee.setLastName(lastName.toUpperCase());
            mentee.setRole(Role.ROLE_MENTEE);
            mentee.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            mentee.setIsApproved(true);
            return userRepository.save(mentee);
        });

        return response(user, jwtService.generateToken(user));
    }
}
