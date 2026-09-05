package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.data.DataStore;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionException;
import com.example.jtcp.session.SessionService;

import java.util.List;

public class ListHandler implements CommandHandler {

    private final SessionService sessionService;
    private final DataStore dataStore;

    public ListHandler(SessionService sessionService, DataStore dataStore) {
        this.sessionService = sessionService;
        this.dataStore = dataStore;
    }

    @Override
    public Response handle(Request request) {

        try {
            Session session = sessionService.validate(request.getSessionId());

            String username = session.getUser().getUsername();

            List<String> keys = dataStore.getKeys(username);

            return new Response(
                    request.getVersion(),
                    Status.OK,
                    String.join(" ", keys)
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