package com.server.mentorgrowth.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionResponse {
    private String id;
    private String userId;
    private String menteeId;
    private String mentorshipId;

    private String scheduledTime;
    private int durationMinutes;
    private String meetingLink;

    private String status;
    private LocalDateTime createdAt;
}
