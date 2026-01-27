package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.MentorshipRepository;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {
    private final UserServiceImpl userService;
    private final MentorshipRepository mentorshipRepository;
    private final ModelMapper modelMapper;

    public Mentorship createMentorship(MentorshipRequest request) {
        UserResponse mentor = userService.findById(request.getMentorId());
        UserResponse mentee = userService.findById(request.getUserId());

        Mentorship mentorship = new Mentorship();
        mentorship.setMentor(modelMapper.map(mentor, User.class));
        mentorship.setMentee(modelMapper.map(mentee, User.class));

        return mentorshipRepository.save(mentorship);
    }

    public Boolean isMentorshipExist(String id) {
        return mentorshipRepository.existsById(id);
    }

    public List<MentorshipResponse> findValidMentorships(String userId, String mentorId) {
        return mentorshipRepository.findValidMentorship(userId, mentorId)
                .stream()
                .map(mentorship -> modelMapper.map(mentorship, MentorshipResponse.class))
                .toList();
    }
}
