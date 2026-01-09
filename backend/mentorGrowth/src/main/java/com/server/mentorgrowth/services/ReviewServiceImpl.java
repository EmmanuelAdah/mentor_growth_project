package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.ReviewRequest;
import com.server.mentorgrowth.dtos.response.ReviewResponse;
import com.server.mentorgrowth.exceptions.NoReviewFoundException;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Review;
import com.server.mentorgrowth.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        if (userService.existById(reviewRequest.getUserId()))
            throw new UserNotFoundException("Invalid user ID: " + reviewRequest.getUserId());

        if (userService.existById(reviewRequest.getMentorId()))
            throw new UserNotFoundException("Invalid mentor ID: " + reviewRequest.getMentorId());

        Review review = new Review();
        review.setUserId(reviewRequest.getUserId());
        review.setMentorId(reviewRequest.getMentorId());
        review.setComment(reviewRequest.getComment());

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewResponse.class);
    }

    @Override
    public List<ReviewResponse> findAll() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty())
            throw new NoReviewFoundException("No reviews found");

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviews)
            reviewResponses.add(modelMapper.map(review, ReviewResponse.class));

        return reviewResponses;
    }

    @Override
    public @Nullable List<ReviewResponse> findById(String id) {
        return reviewRepository.findById(id)
                .stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .collect(Collectors.toList());
    }
}
