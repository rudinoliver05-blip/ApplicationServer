package com.example.jtcp.tests;

import com.example.jtcp.session.*;
import com.example.jtcp.userdetails.User;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    @Test
    void validSessionIsAccepted() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        User user = new User("alice", "password123");

        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plus(Duration.ofMinutes(30));

        Session session =
                new Session(
                        "session-1",
                        user,
                        createdAt,
                        expiresAt
                );

        sessionStore.save(session);

        Session result =
                sessionService.validate("session-1");

        assertSame(session, result);
    }

    @Test
    void unknownSessionIsRejected() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        assertThrows(
                SessionException.class,
                () -> sessionService.validate("does-not-exist")
        );
    }

    @Test
    void expiredSessionIsRejected() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        User user = new User("alice", "password123");

        Instant createdAt =
                Instant.now().minus(Duration.ofMinutes(10));

        Instant expiresAt =
                Instant.now().minus(Duration.ofMinutes(1));

        Session session =
                new Session(
                        "expired-session",
                        user,
                        createdAt,
                        expiresAt
                );

        sessionStore.save(session);

        assertThrows(
                SessionException.class,
                () -> sessionService.validate("expired-session")
        );
    }

    @Test
    void expiredSessionIsRemovedAfterValidation() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        User user = new User("alice", "password123");

        Instant createdAt =
                Instant.now().minus(Duration.ofMinutes(10));

        Instant expiresAt =
                Instant.now().minus(Duration.ofMinutes(1));

        Session session =
                new Session(
                        "expired-session",
                        user,
                        createdAt,
                        expiresAt
                );

        sessionStore.save(session);

        assertThrows(
                SessionException.class,
                () -> sessionService.validate("expired-session")
        );

        assertNull(
                sessionStore.find("expired-session")
        );
    }

    @Test
    void logoutRemovesSession() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        User user = new User("alice", "password123");

        Session session =
                new Session(
                        "session-1",
                        user,
                        Instant.now(),
                        Instant.now().plus(Duration.ofMinutes(30))
                );

        sessionStore.save(session);

        sessionService.logout("session-1");

        assertNull(
                sessionStore.find("session-1")
        );
    }

    @Test
    void loggedOutSessionCannotBeValidated() {

        SessionStore sessionStore = new InMemorySessionStore();
        SessionService sessionService = new SessionService(sessionStore);

        User user = new User("alice", "password123");

        Session session =
                new Session(
                        "session-1",
                        user,
                        Instant.now(),
                        Instant.now().plus(Duration.ofMinutes(30))
                );

        sessionStore.save(session);

        sessionService.logout("session-1");

        assertThrows(
                SessionException.class,
                () -> sessionService.validate("session-1")
        );
    }
}
