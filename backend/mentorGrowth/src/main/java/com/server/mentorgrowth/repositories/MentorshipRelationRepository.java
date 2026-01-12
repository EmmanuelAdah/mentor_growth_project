package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.MentorshipRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorshipRelationRepository extends JpaRepository<MentorshipRelation, Integer> {
}
