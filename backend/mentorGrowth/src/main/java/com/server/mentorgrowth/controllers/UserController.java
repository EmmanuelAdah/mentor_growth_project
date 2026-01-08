package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(userService.saveUser(request));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.existByEmail(email));
    }

    @GetMapping("/mentors/all")
    public ResponseEntity<List<UserResponse>> getAllMentors(){
        return ResponseEntity.ok(userService.getAllMentors());
    }

    @GetMapping("/mentees/all")
    public ResponseEntity<List<UserResponse>> getAllMentees(){
        return ResponseEntity.ok(userService.getAllMentees());
    }
}
