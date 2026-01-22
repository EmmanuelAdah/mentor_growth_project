package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.exceptions.NotExistingMentorshipException;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Session;
import com.server.mentorgrowth.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserServiceImpl userService;
    private final MentorshipServiceImpl mentorshipService;
//    private final ModelMapper modelMapper;

    public @Nullable SessionResponse createSession(SessionRequest request) {
        Boolean isExistingUser = userService.existById(request.getUserId());
        Boolean isExistingMentee = userService.existById(request.getMenteeId());
        Boolean isMentorshipExist = mentorshipService.isMentorshipExist(request.getMentorshipId());

        if (!isExistingUser || !isExistingMentee) {
            throw new UserNotFoundException("User not found with id: " + request.getUserId() + " or mentee id: " + request.getMenteeId());
        }
        if (!isMentorshipExist) {
            throw new NotExistingMentorshipException("No existing mentorship relation found");
        }
        sessionRepository.save(new Session());

        return new SessionResponse();
    }
}
