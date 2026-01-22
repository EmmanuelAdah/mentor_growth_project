package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class SessionRequest {
    private String userId;
    private String menteeId;
    private String mentorshipId;

    private String scheduledTime;
    private int durationMinutes;
    private String meetingLink;
}
