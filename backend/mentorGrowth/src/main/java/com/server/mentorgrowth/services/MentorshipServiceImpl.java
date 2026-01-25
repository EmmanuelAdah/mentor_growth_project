package com.server.mentorgrowth.services;

import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.repositories.MentorshipRepository;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {
    private final MentorshipRepository mentorshipRepository;

    public void createMentorship(String mentorId, String menteeId) {
        mentorshipRepository.save(new Mentorship());
    }

    public Boolean isMentorshipExist(String id) {
        return mentorshipRepository.existsById(id);
    }
}
