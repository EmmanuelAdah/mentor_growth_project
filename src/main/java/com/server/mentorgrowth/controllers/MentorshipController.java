package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.requests.SetGoalsRequest;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mentorship")
@RequiredArgsConstructor
public class MentorshipController {
    private final MentorshipService mentorshipService;

    @PostMapping("/create")
    public ResponseEntity<MentorshipResponse> createMentorship(@Valid @RequestBody MentorshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipService.createMentorship(request));
    }

    @PostMapping("/set/goals")
    public ResponseEntity<MentorshipResponse> createMentorshipGoals(@Valid @RequestBody SetGoalsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipService.createMentorshipGoals(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorshipResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(mentorshipService.findById(id));
    }
}
