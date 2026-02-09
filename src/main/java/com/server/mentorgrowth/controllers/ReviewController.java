package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.ReviewRequest;
import com.server.mentorgrowth.dtos.response.ReviewResponse;
import com.server.mentorgrowth.services.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
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
    public void deleteById(@PathVariable String id){
        reviewService.deleteById(id);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteByUserId(@PathVariable String id){
        reviewService.deleteByUserId(id);
    }
}
