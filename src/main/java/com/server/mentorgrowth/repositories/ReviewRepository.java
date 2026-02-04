package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Optional<Review> findByMentorId(String mentorId);
    long deleteByUserId(String userId);
}
