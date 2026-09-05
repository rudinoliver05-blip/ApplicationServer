package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionException;
import com.example.jtcp.session.SessionService;

public class WhoAmIHandler implements CommandHandler {

    private final SessionService sessionService;

    public WhoAmIHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Response handle(Request request) {

        try {
            Session session = sessionService.validate(request.getSessionId());

            return new Response(
                    request.getVersion(),
                    Status.OK,
                    session.getUser().getUsername()
            );

        } catch (SessionException e) {

            return new Response(
                    request.getVersion(),
                    Status.UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }
}