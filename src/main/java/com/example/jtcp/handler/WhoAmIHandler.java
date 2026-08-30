package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;

public class WhoAmIHandler implements CommandHandler {

    private final SessionStore sessionStore;

    public WhoAmIHandler(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
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

        return new Response(
                request.getVersion(),
                Status.OK,
                session.getUser().getUsername()
        );
    }
}