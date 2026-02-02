package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.MentorshipNotFoundException;
import com.server.mentorgrowth.models.Goal;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.MentorshipRepository;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {
    private final UserServiceImpl userService;
    private final MentorshipRepository mentorshipRepository;
    private final ModelMapper modelMapper;

    public MentorshipResponse createMentorship(MentorshipRequest request) {
        log.info("There a mentorship call with this {}", request);
        UserResponse mentor = userService.findById(request.getMentorId());
        UserResponse mentee = userService.findById(request.getUserId());

        Mentorship mentorship = new Mentorship();

        mentorship.setMentor(modelMapper.map(mentor, User.class));
        mentorship.setMentee(modelMapper.map(mentee, User.class));

        Mentorship savedMentorship = mentorshipRepository.save(mentorship);

        return modelMapper.map(savedMentorship, MentorshipResponse.class);
    }

    public @Nullable MentorshipResponse findById(String id) {
        return Objects.requireNonNull(mentorshipRepository.findById(id))
                .map(mentorship -> modelMapper.map(mentorship, MentorshipResponse.class))
                .orElseThrow(() -> new MentorshipNotFoundException("Mentorship not found with id: " + id));
    }

    public @Nullable Mentorship createMentorshipGoals(List<Goal> goals) {
        return null;
    }
}
