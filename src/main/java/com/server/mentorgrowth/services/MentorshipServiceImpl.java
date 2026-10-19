package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.GoalRequest;
import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.requests.SetGoalsRequest;
import com.server.mentorgrowth.dtos.response.GoalResponse;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.MentorshipNotFoundException;
import com.server.mentorgrowth.models.Goal;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.models.MentorshipStatus;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.MentorshipRepository;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {
    private final UserServiceImpl userService;
    private final MentorshipRepository mentorshipRepository;

    @Override
    @Transactional
    public MentorshipResponse createMentorship(MentorshipRequest request) {
        log.info("Creating mentorship between mentee {} and mentor {}", request.getUserId(), request.getMentorId());

        User mentee = userService.getUserById(request.getUserId());
        User mentor = userService.getUserById(request.getMentorId());

        Mentorship mentorship = Mentorship.builder()
                .mentee(mentee)
                .mentor(mentor)
                .status(MentorshipStatus.PENDING)
                .build();

        Mentorship savedMentorship = mentorshipRepository.save(mentorship);
        return toResponse(savedMentorship);
    }

    @Override
    @Transactional(readOnly = true)
    public MentorshipResponse findById(String id) {
        return toResponse(getMentorshipEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Mentorship getMentorshipEntity(String id) {
        return mentorshipRepository.findById(id)
                .orElseThrow(() -> new MentorshipNotFoundException("Mentorship not found with id: " + id));
    }

    @Override
    @Transactional
    public MentorshipResponse createMentorshipGoals(SetGoalsRequest request) {
        Mentorship mentorship = getMentorshipEntity(request.getMentorshipId());

        for (GoalRequest goalRequest : request.getGoals()) {
            Goal goal = Goal.builder()
                    .mentorship(mentorship)
                    .title(goalRequest.getTitle())
                    .description(goalRequest.getDescription())
                    .targetDate(goalRequest.getTargetDate())
                    .status("PENDING")
                    .build();
            mentorship.getGoals().add(goal);
        }

        Mentorship saved = mentorshipRepository.save(mentorship);
        return toResponse(saved);
    }

    private MentorshipResponse toResponse(Mentorship mentorship) {
        List<GoalResponse> goals = mentorship.getGoals() == null
                ? List.of()
                : mentorship.getGoals().stream().map(this::toGoalResponse).toList();

        return MentorshipResponse.builder()
                .id(mentorship.getId())
                .mentee(toUserResponse(mentorship.getMentee()))
                .mentor(toUserResponse(mentorship.getMentor()))
                .goals(goals)
                .status(mentorship.getStatus())
                .createdAt(mentorship.getCreatedAt())
                .build();
    }

    private GoalResponse toGoalResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .mentorshipId(goal.getMentorship() != null ? goal.getMentorship().getId() : null)
                .targetDate(goal.getTargetDate() != null ? goal.getTargetDate().atStartOfDay() : null)
                .status(goal.getStatus())
                .createdAt(goal.getCreatedAt() != null ? goal.getCreatedAt().toString() : null)
                .updatedAt(goal.getUpdatedAt() != null ? goal.getUpdatedAt().toString() : null)
                .build();
    }

    private UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profession(user.getProfession())
                .linkedin(user.getLinkedin())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
