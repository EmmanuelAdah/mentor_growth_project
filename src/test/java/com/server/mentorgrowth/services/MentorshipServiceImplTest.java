package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.GoalRequest;
import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.requests.SetGoalsRequest;
import com.server.mentorgrowth.enums.MentorshipStatus;
import com.server.mentorgrowth.enums.Role;
import com.server.mentorgrowth.exceptions.MentorshipNotFoundException;
import com.server.mentorgrowth.models.Goal;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.MentorshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipServiceImplTest {
    private static final String MENTEE_ID = "mentee-1";
    private static final String MENTOR_ID = "mentor-1";
    private static final String MENTORSHIP_ID = "mentorship-1";

    @Mock
    private UserServiceImpl userService;
    @Mock
    private MentorshipRepository mentorshipRepository;
    @InjectMocks
    private MentorshipServiceImpl service;

    private User mentee;
    private User mentor;
    private Mentorship mentorship;

    @BeforeEach
    void setUp() {
        mentee = user(MENTEE_ID, Role.ROLE_MENTEE);
        mentor = user(MENTOR_ID, Role.ROLE_MENTOR);
        mentorship = Mentorship.builder()
                .id(MENTORSHIP_ID)
                .mentee(mentee)
                .mentor(mentor)
                .goals(new ArrayList<>())
                .status(MentorshipStatus.ACTIVE)
                .build();
    }

    @Test
    void createMentorshipCreatesPendingRelationship() {
        MentorshipRequest request = new MentorshipRequest();
        request.setMentorId(MENTOR_ID);
        when(userService.getUserById(MENTEE_ID)).thenReturn(mentee);
        when(userService.getUserById(MENTOR_ID)).thenReturn(mentor);
        when(mentorshipRepository.save(any(Mentorship.class))).thenAnswer(invocation -> {
            Mentorship saved = invocation.getArgument(0);
            saved.setId(MENTORSHIP_ID);
            return saved;
        });

        var response = service.createMentorship(request, MENTEE_ID);

        assertEquals(MENTORSHIP_ID, response.getId());
        assertEquals(MentorshipStatus.PENDING, response.getStatus());
        assertEquals(MENTEE_ID, response.getMentee().getId());
        assertEquals(MENTOR_ID, response.getMentor().getId());
    }

    @Test
    void createMentorshipRejectsWrongParticipantRoles() {
        MentorshipRequest request = new MentorshipRequest();
        request.setMentorId(MENTOR_ID);
        when(userService.getUserById(MENTEE_ID)).thenReturn(mentor);
        when(userService.getUserById(MENTOR_ID)).thenReturn(mentee);

        assertThrows(AccessDeniedException.class, () -> service.createMentorship(request, MENTEE_ID));

        verify(mentorshipRepository, never()).save(any(Mentorship.class));
    }

    @Test
    void createGoalsPersistsGoalsAndReturnsTheirDetails() {
        GoalRequest goalRequest = GoalRequest.builder()
                .title("Complete portfolio")
                .description("Publish three projects")
                .targetDate(LocalDate.of(2026, 12, 31))
                .build();
        SetGoalsRequest request = new SetGoalsRequest();
        request.setMentorshipId(MENTORSHIP_ID);
        request.setGoals(List.of(goalRequest));
        when(mentorshipRepository.findById(MENTORSHIP_ID)).thenReturn(Optional.of(mentorship));
        when(mentorshipRepository.save(mentorship)).thenReturn(mentorship);

        var response = service.createMentorshipGoals(request, MENTEE_ID);

        assertEquals(1, response.getGoals().size());
        assertEquals("Complete portfolio", response.getGoals().getFirst().getTitle());
        assertEquals("PENDING", response.getGoals().getFirst().getStatus());
        ArgumentCaptor<Mentorship> captor = ArgumentCaptor.forClass(Mentorship.class);
        verify(mentorshipRepository).save(captor.capture());
        Goal savedGoal = captor.getValue().getGoals().getFirst();
        assertEquals(mentorship, savedGoal.getMentorship());
        assertEquals(LocalDate.of(2026, 12, 31), savedGoal.getTargetDate());
    }

    @Test
    void findByIdRejectsNonParticipant() {
        when(mentorshipRepository.findById(MENTORSHIP_ID)).thenReturn(Optional.of(mentorship));

        assertThrows(AccessDeniedException.class, () -> service.findById(MENTORSHIP_ID, "outsider"));
    }

    @Test
    void getMentorshipEntityRejectsMissingRelationship() {
        when(mentorshipRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(MentorshipNotFoundException.class, () -> service.getMentorshipEntity("missing"));
    }

    private User user(String id, Role role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        return user;
    }
}
