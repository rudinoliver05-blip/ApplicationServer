package com.example.jtcp.session;

public interface SessionStore {

    void save(Session session);

    Session find(String sessionId);

    void remove(String sessionId);
}