package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.services.MentorshipServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentorship")
@RequiredArgsConstructor
public class MentorshipController {
    private final MentorshipServiceImpl mentorshipService;

    @RequestMapping("/create")
    public ResponseEntity<Mentorship> createMentroship(@RequestBody MentorshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipService.createMentorship(request));
    }
}
