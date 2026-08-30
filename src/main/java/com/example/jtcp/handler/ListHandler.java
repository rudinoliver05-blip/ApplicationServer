package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.data.DataStore;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;

import java.util.List;

public class ListHandler implements CommandHandler {

    private final SessionStore sessionStore;
    private final DataStore dataStore;

    public ListHandler(SessionStore sessionStore, DataStore dataStore) {
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

        String username = session.getUser().getUsername();

        List<String> keys = dataStore.getKeys(username);

        if (keys.isEmpty()) {
            return new Response(
                    request.getVersion(),
                    Status.NOT_FOUND,
                    "No data found"
            );
        }

        String result = String.join(", ", keys);

        return new Response(
                request.getVersion(),
                Status.OK,
                result
        );
    }
}