package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class MentorshipRequest {
    private String userId;
    private String mentorId;
}
