package com.server.mentorgrowth.dtos.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        String id,
        String userId,
        String message,
        LocalDateTime createdAt
) { }
