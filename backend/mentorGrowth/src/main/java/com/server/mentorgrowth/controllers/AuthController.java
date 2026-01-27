package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.LoginRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.exceptions.ServerConnectionException;
import com.server.mentorgrowth.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> createUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.saveUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> userLogin(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login/google")
    public void googleLogin(HttpServletResponse  response) {
        try {
            response.sendRedirect("/oauth2/authorization/google");
        } catch (IOException e) {
            throw new ServerConnectionException("An error occurred while trying to redirect to google");
        }
    }

    @GetMapping("/google/oauth2/authenticated")
    public ResponseEntity<UserAuthResponse> googleAuth(OAuth2AuthenticationToken authToken) {
        OAuth2User oAuth2User = authToken.getPrincipal();
        String email = Objects.requireNonNull(oAuth2User).getAttribute("email");
        String name = Objects.requireNonNull(oAuth2User.getAttribute("name"));
        return ResponseEntity.ok(authService.googleLogin(email, name));
    }
}
