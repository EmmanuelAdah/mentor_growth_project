package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class ReviewRequest {
    private String userId;
    private String mentorId;
    private String comment;
}
