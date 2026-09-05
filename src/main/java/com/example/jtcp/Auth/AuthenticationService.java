package com.example.jtcp.Auth;

import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;
import com.example.jtcp.userdetails.User;
import com.example.jtcp.userdetails.UserStore;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class AuthenticationService {

    private final UserStore userStore;
    private final SessionStore sessionStore;
    private final Duration sessionLifetime;

    public AuthenticationService(
            UserStore userStore,
            SessionStore sessionStore,
            Duration sessionLifetime
    ) {
        this.userStore = userStore;
        this.sessionStore = sessionStore;
        this.sessionLifetime = sessionLifetime;
    }

    public Session authenticate(String username, String password) {

        User user = userStore.find(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException(
                    "Invalid username or password"
            );
        }

        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plus(sessionLifetime);

        String sessionId = UUID.randomUUID().toString();

        Session session = new Session(
                sessionId,
                user,
                createdAt,
                expiresAt
        );

        sessionStore.save(session);

        return session;
    }
}