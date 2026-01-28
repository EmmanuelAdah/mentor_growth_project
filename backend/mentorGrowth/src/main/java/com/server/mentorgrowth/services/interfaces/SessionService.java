package com.server.mentorgrowth.services.interfaces;

import org.jspecify.annotations.Nullable;

public interface SessionService {

    @Nullable String createSession(String mentorshipId);
}
