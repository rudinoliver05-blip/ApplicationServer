package com.example.jtcp.tests;

import com.example.jtcp.Auth.AuthenticationException;
import com.example.jtcp.Auth.AuthenticationService;
import com.example.jtcp.session.InMemorySessionStore;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;
import com.example.jtcp.userdetails.InMemoryUserStore;
import com.example.jtcp.userdetails.User;
import com.example.jtcp.userdetails.UserStore;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Test
    void authenticateWithValidCredentialsCreatesSession() {

        UserStore userStore = new InMemoryUserStore();
        SessionStore sessionStore = new InMemorySessionStore();

        userStore.createIfAbsent(
                new User("alice", "password123")
        );

        AuthenticationService authenticationService =
                new AuthenticationService(
                        userStore,
                        sessionStore,
                        Duration.ofMinutes(30)
                );

        Session session =
                authenticationService.authenticate(
                        "alice",
                        "password123"
                );

        assertNotNull(session);
        assertNotNull(session.getSessionId());
        assertEquals("alice", session.getUser().getUsername());

        assertSame(
                session,
                sessionStore.find(session.getSessionId())
        );
    }

    @Test
    void authenticateWithWrongPasswordFails() {

        UserStore userStore = new InMemoryUserStore();
        SessionStore sessionStore = new InMemorySessionStore();

        userStore.createIfAbsent(
                new User("alice", "password123")
        );

        AuthenticationService authenticationService =
                new AuthenticationService(
                        userStore,
                        sessionStore,
                        Duration.ofMinutes(30)
                );

        assertThrows(
                AuthenticationException.class,
                () -> authenticationService.authenticate(
                        "alice",
                        "wrongPassword"
                )
        );
    }

    @Test
    void authenticateUnknownUserFails() {

        UserStore userStore = new InMemoryUserStore();
        SessionStore sessionStore = new InMemorySessionStore();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        userStore,
                        sessionStore,
                        Duration.ofMinutes(30)
                );

        assertThrows(
                AuthenticationException.class,
                () -> authenticationService.authenticate(
                        "unknown",
                        "password123"
                )
        );
    }
}