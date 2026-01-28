package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.models.Goal;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.services.MentorshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorship")
@RequiredArgsConstructor
public class MentorshipController {
    private final MentorshipServiceImpl mentorshipService;

    @PostMapping("/create")
    public ResponseEntity<Mentorship> createMentroship(@RequestBody MentorshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipService.createMentorship(request));
    }

    @PostMapping("/set/goals")
    public ResponseEntity<Mentorship> createMentorshipGoals(@RequestBody List<Goal> goals) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipService.createMentorshipGoals(goals));
    }
}
