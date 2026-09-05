package com.example.jtcp.handler;

import com.example.jtcp.Auth.AuthenticationException;
import com.example.jtcp.Auth.AuthenticationService;
import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;

public class LoginHandler implements CommandHandler {

    private final AuthenticationService authenticationService;

    public LoginHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Response handle(Request request) {

        String username = request.getArguments().get(0);
        String password = request.getArguments().get(1);

        try {
            Session session =
                    authenticationService.authenticate(username, password);

            return new Response(
                    request.getVersion(),
                    Status.OK,
                    session.getSessionId()
            );

        } catch (AuthenticationException e) {

            return new Response(
                    request.getVersion(),
                    Status.UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }
}