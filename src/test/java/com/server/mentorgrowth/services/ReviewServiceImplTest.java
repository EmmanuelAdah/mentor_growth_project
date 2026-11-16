package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.ReviewRequest;
import com.server.mentorgrowth.dtos.response.ReviewResponse;
import com.server.mentorgrowth.enums.Role;
import com.server.mentorgrowth.exceptions.NoReviewFoundException;
import com.server.mentorgrowth.models.Review;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    private static final String MENTEE_ID = "mentee-1";
    private static final String MENTOR_ID = "mentor-1";

    @Mock
    private UserServiceImpl userService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ReviewServiceImpl service;

    private User mentee;
    private User mentor;

    @BeforeEach
    void setUp() {
        mentee = user(MENTEE_ID, Role.ROLE_MENTEE);
        mentor = user(MENTOR_ID, Role.ROLE_MENTOR);
    }

    @Test
    void createReviewPersistsReviewForMenteeAndMapsResponse() {
        ReviewRequest request = new ReviewRequest();
        request.setMentorId(MENTOR_ID);
        request.setComment("Helpful mentoring");
        ReviewResponse expected = new ReviewResponse();
        expected.setMentorId(MENTOR_ID);
        expected.setComment(request.getComment());
        when(userService.getUserById(MENTEE_ID)).thenReturn(mentee);
        when(userService.getUserById(MENTOR_ID)).thenReturn(mentor);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Review.class), eq(ReviewResponse.class))).thenReturn(expected);

        ReviewResponse response = service.createReview(request, MENTEE_ID);

        assertEquals(expected, response);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());
        assertEquals(MENTEE_ID, captor.getValue().getUserId());
        assertEquals(MENTOR_ID, captor.getValue().getMentorId());
        assertEquals("Helpful mentoring", captor.getValue().getComment());
    }

    @Test
    void createReviewRejectsNonMenteeAndDoesNotSave() {
        ReviewRequest request = new ReviewRequest();
        request.setMentorId(MENTOR_ID);
        when(userService.getUserById(MENTEE_ID)).thenReturn(mentor);
        when(userService.getUserById(MENTOR_ID)).thenReturn(mentor);

        assertThrows(AccessDeniedException.class, () -> service.createReview(request, MENTEE_ID));

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void findAllRejectsEmptyReviewCollection() {
        when(reviewRepository.findAll()).thenReturn(List.of());

        assertThrows(NoReviewFoundException.class, service::findAll);
    }

    @Test
    void findByIdRejectsUnknownReview() {
        when(reviewRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(NoReviewFoundException.class, () -> service.findById("missing"));
    }

    @Test
    void deleteByIdRejectsDeletingAnotherUsersReview() {
        Review review = new Review();
        review.setUserId("another-user");
        when(reviewRepository.findById("review-1")).thenReturn(Optional.of(review));

        assertThrows(AccessDeniedException.class, () -> service.deleteById("review-1", MENTEE_ID));

        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteByUserIdRejectsWhenNoReviewsWereDeleted() {
        when(reviewRepository.deleteByUserId(MENTEE_ID)).thenReturn(0L);

        assertThrows(NoReviewFoundException.class, () -> service.deleteByUserId(MENTEE_ID));
    }

    private User user(String id, Role role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        return user;
    }
}
