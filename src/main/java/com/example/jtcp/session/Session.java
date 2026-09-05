package com.example.jtcp.session;

import com.example.jtcp.userdetails.User;

import java.time.Instant;

public final class Session {

    private final String sessionId;
    private final User user;
    private final Instant createdAt;
    private final Instant expiresAt;

    public Session(
            String sessionId,
            User user,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.sessionId = sessionId;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}