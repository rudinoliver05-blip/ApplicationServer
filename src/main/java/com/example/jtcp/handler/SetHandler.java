package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.data.DataStore;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;
import com.example.jtcp.data.InMemoryDataStore;

public class SetHandler implements CommandHandler {

    private final SessionStore sessionStore;
    private final DataStore dataStore;

    public SetHandler(SessionStore sessionStore, DataStore dataStore) {

        this.sessionStore = sessionStore;
        this.dataStore = dataStore;
    }

    @Override
    public Response handle(Request request) {

        String sessionId = request.getSessionId();
        Session session = sessionStore.find(sessionId);

        if (session == null) {
            return new Response(
                    request.getVersion(),
                    Status.UNAUTHORIZED,
                    "Invalid session"
            );
        }

        String key = request.getArguments().get(0);
        String value = request.getArguments().get(1);

        String username = session.getUser().getUsername();

        dataStore.set(username, key, value);

        return new Response(
                request.getVersion(),
                Status.OK,
                "Value set successfully"
        );
    }
}