package com.server.mentorgrowth.dtos.requests;

import com.google.type.DateTime;
import lombok.Data;

@Data
public class SessionRequest {
    private String userId;
    private String menteeId;
    private String mentorshipId;

    private DateTime scheduledTime;
    private int durationMinutes;
    private String meetingLink;
}
