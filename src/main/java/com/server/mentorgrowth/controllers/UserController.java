package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.services.UserServiceImpl;
import com.server.mentorgrowth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.findById(user.getId()));
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

    @PutMapping(value = "/update/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateProfilePicture(
            @AuthenticationPrincipal User user,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePicture(user.getId(), file));
    }
}
