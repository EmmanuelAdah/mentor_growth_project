package com.server.mentorgrowth.services;

import com.server.mentorgrowth.repositories.MentorshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {
    private final MentorshipRepository mentorshipRepository;

    public Boolean isMentorshipExist(String id) {
        return mentorshipRepository.existsById(id);
    }
}
