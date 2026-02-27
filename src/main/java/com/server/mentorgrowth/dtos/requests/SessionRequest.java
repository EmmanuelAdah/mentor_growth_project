package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class SessionRequest {
    private String mentorshipId;
    private String summary;
    private String startTime;
    private int durationInMinutes;
}
