package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {

}
