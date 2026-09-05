package com.example.jtcp.tests;

import com.example.jtcp.data.DataStore;
import com.example.jtcp.data.InMemoryDataStore;
import com.example.jtcp.handler.GetHandler;
import com.example.jtcp.handler.SetHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.protocol.Command;
import com.example.jtcp.session.*;
import com.example.jtcp.userdetails.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataIsolationTest {

    @Test
    void usersHaveSeparateData() {

        SessionStore sessionStore = new InMemorySessionStore();
        DataStore dataStore = new InMemoryDataStore();

        SessionService sessionService =
                new SessionService(sessionStore);

        Session aliceSession =
                new Session(
                        "alice-session",
                        new User("alice", "password"),
                        Instant.now(),
                        Instant.now().plus(Duration.ofMinutes(30))
                );

        Session bobSession =
                new Session(
                        "bob-session",
                        new User("bob", "password"),
                        Instant.now(),
                        Instant.now().plus(Duration.ofMinutes(30))
                );

        sessionStore.save(aliceSession);
        sessionStore.save(bobSession);

        SetHandler setHandler =
                new SetHandler(sessionService, dataStore);

        GetHandler getHandler =
                new GetHandler(sessionService, dataStore);

        Request aliceSet =
                new Request(
                        "JTCP/1.0",
                        Command.SET,
                        "alice-session",
                        List.of("secret", "AliceSecret")
                );

        Response setResponse =
                setHandler.handle(aliceSet);

        assertEquals(Status.OK, setResponse.getStatus());

        Request bobGet =
                new Request(
                        "JTCP/1.0",
                        Command.GET,
                        "bob-session",
                        List.of("secret")
                );

        Response bobResponse =
                getHandler.handle(bobGet);

        assertEquals(
                Status.NOT_FOUND,
                bobResponse.getStatus()
        );
    }
}