package com.server.mentorgrowth.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthResponse {
    private String id;
    private String token;
}
