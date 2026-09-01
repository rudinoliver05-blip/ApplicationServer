package com.example.jtcp.session;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionStore implements SessionStore {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    @Override
    public Session find(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }
}