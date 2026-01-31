//package com.server.mentorgrowth.controllers;
//
//import com.server.mentorgrowth.services.SessionServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/session")
//@RequiredArgsConstructor
//public class SessionController {
//    private final SessionServiceImpl sessionService;
//
//    @PostMapping("/create/{mentorshipId}")
//    public ResponseEntity<String> createSession(@PathVariable String mentorshipId) {
//        return ResponseEntity.ok(sessionService.createSession(mentorshipId));
//    }
//}
