package com.example.jtcp.session;

import com.example.jtcp.userdetails.User;

public class Session {

    private final String sessionId;
    private final User user;

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }
}