package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import org.jspecify.annotations.Nullable;

public interface SessionService {

    @Nullable String createSession(SessionRequest request);
}
