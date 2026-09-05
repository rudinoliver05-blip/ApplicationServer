package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.data.DataStore;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionException;
import com.example.jtcp.session.SessionService;

public class SetHandler implements CommandHandler {

    private final SessionService sessionService;
    private final DataStore dataStore;

    public SetHandler(SessionService sessionService, DataStore dataStore) {
        this.sessionService = sessionService;
        this.dataStore = dataStore;
    }

    @Override
    public Response handle(Request request) {

        try {
            Session session = sessionService.validate(request.getSessionId());

            String username = session.getUser().getUsername();
            String key = request.getArguments().get(0);
            String value = request.getArguments().get(1);

            dataStore.set(username, key, value);

            return new Response(
                    request.getVersion(),
                    Status.OK,
                    "Value stored"
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