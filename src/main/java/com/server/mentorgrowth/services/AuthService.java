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
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
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
    private final ModelMapper modelMapper;

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

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid username or password");

        String token = jwtService.generateToken(user);
        return response(user, token);
    }

    private UserAuthResponse response(User user, String token) {
        return UserAuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name()
                        .split("_")[1])
                .token(token)
                .build();
    }

    public @Nullable UserAuthResponse googleLogin(String email, String name) {
        String[] names = Objects.requireNonNull(name).split(" ");
        log.info("Google login from email: {} and name: {} {}", email, names[0], names[1]);

        User user = new User();
        user.setEmail(email);
        user.setFirstName(names[0]);
        user.setLastName(names[1]);

        User savedUser = userRepository.save(user);
        return modelMapper.map(Objects.requireNonNull(savedUser), UserAuthResponse.class);
    }
}
