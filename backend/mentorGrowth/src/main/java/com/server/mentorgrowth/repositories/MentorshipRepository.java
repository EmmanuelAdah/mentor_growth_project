package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Mentorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MentorshipRepository extends JpaRepository<Mentorship, Integer> {
    Boolean existsById(String id);

    @Query("SELECT m FROM Mentorship m WHERE m.mentee.id = :userId AND m.mentor.id = :mentorId")
    Optional<Mentorship> findValidMentorship(String userId, String mentorId);
}
