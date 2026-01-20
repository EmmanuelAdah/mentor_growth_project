package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Mentorship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorshipRepository extends JpaRepository<Mentorship, Integer> {
}
