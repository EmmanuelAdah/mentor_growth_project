package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface SessionService {

    SessionResponse createSession(SessionRequest request) throws GeneralSecurityException, IOException;
}
