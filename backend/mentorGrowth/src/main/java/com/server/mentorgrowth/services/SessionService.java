package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;

public interface SessionService {

    SessionResponse createSession(SessionRequest request);
}
