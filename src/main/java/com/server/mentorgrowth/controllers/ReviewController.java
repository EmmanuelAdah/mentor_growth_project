package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.ReviewRequest;
import com.server.mentorgrowth.dtos.response.ReviewResponse;
import com.server.mentorgrowth.services.ReviewServiceImpl;
import com.server.mentorgrowth.models.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest, user.getId()));
    }

    @GetMapping("/get/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ReviewResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @GetMapping("/mentor/{id}")
    public ResponseEntity<List<ReviewResponse>> findByUserId(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.findByMentorId(id));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id, @AuthenticationPrincipal User user){
        reviewService.deleteById(id, user.getId());
    }

    @DeleteMapping("/user")
    public void deleteByUserId(@AuthenticationPrincipal User user){
        reviewService.deleteByUserId(user.getId());
    }
}
