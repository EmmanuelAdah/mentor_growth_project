package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.services.interfaces.SessionService;
import com.server.mentorgrowth.models.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<SessionResponse> createSession(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSession(request, user.getId()));
    }
}
