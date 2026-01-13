package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.LoginRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserAuthResponse;
import com.server.mentorgrowth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
