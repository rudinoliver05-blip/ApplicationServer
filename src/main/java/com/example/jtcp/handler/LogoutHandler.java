package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.SessionService;

public class LogoutHandler implements CommandHandler {

    private final SessionService sessionService;

    public LogoutHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Response handle(Request request) {

        sessionService.logout(request.getSessionId());

        return new Response(
                request.getVersion(),
                Status.OK,
                "Logout successful"
        );
    }
}