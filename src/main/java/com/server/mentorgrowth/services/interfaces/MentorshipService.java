package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.MentorshipRequest;
import com.server.mentorgrowth.dtos.requests.SetGoalsRequest;
import com.server.mentorgrowth.dtos.response.MentorshipResponse;
import com.server.mentorgrowth.models.Mentorship;

public interface MentorshipService {
    MentorshipResponse createMentorship(MentorshipRequest request);
    MentorshipResponse findById(String id);
    Mentorship getMentorshipEntity(String id);
    MentorshipResponse createMentorshipGoals(SetGoalsRequest request);
}
