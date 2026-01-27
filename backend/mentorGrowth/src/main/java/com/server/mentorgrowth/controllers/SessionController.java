package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.services.SessionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionServiceImpl sessionService;

    @PostMapping("/create")
    public ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(sessionService.createSession(request));
    }
}
