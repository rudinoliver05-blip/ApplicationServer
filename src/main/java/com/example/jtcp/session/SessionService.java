package com.example.jtcp.session;

import java.time.Instant;

public class SessionService {

    private final SessionStore sessionStore;

    public SessionService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public Session validate(String sessionId) {

        Session session = sessionStore.find(sessionId);

        if (session == null) {
            throw new SessionException("Invalid session");
        }

        Instant now = Instant.now();

        if (!now.isBefore(session.getExpiresAt())) {
            sessionStore.remove(sessionId);
            throw new SessionException("Session expired");
        }

        return session;
    }

    public void logout(String sessionId) {
        sessionStore.remove(sessionId);
    }
}