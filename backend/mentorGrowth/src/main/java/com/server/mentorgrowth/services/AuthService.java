package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.LoginRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.exceptions.InvalidCredentialsException;
import com.server.mentorgrowth.exceptions.UserAlreadyExistException;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import static com.server.mentorgrowth.utils.Mapper.map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserAuthResponse saveUser(RegisterRequest request){
        request.setPassword(Objects.requireNonNull(passwordEncoder.encode(request.getPassword())));

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistException("User already exists");
        }
        User user = userRepository.save(map(request));
        return response(user, jwtService.generateToken(user));
    }

    public UserAuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        log.info("User email: {} logged in password: {}", user.getEmail(), user.getPassword());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid username or password");

        String token = jwtService.generateToken(user);
        return response(user, token);
    }

    private UserAuthResponse response(User user, String token) {
        return UserAuthResponse.builder()
                .token(token)
                .id(user.getId())
                .build();
    }
}
