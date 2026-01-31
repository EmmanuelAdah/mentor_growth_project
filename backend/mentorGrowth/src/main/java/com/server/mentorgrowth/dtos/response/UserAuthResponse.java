package com.server.mentorgrowth.dtos.response;

import lombok.Builder;

@Builder
public record UserAuthResponse(
        String id,
        String token
) { }
