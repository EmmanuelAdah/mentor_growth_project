package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.ReviewRequest;
import com.server.mentorgrowth.dtos.response.ReviewResponse;
import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest reviewRequest);
    List<ReviewResponse> findAll();
    ReviewResponse findById(String id);
    List<ReviewResponse> findByMentorId(String id);
    void deleteById(String id);
    void deleteByUserId(String id);
}
