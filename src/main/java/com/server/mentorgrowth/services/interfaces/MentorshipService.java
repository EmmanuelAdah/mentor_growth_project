package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.requests.SetGoalsRequest;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.models.Mentorship;

public interface MentorshipService {
    MentorshipResponse createMentorship(MentorshipRequest request, String currentUserId);
    MentorshipResponse findById(String id, String currentUserId);
    Mentorship getMentorshipEntity(String id);
    MentorshipResponse createMentorshipGoals(SetGoalsRequest request, String currentUserId);
}
