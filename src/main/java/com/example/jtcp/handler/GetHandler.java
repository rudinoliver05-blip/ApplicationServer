package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.data.DataStore;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;

import java.util.List;

public class GetHandler implements CommandHandler {
    private final SessionStore sessionStore;
    private final DataStore dataStore;

    public GetHandler(SessionStore sessionStore, DataStore dataStore) {

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

        List<String> argument=request.getArguments();
        String found=dataStore.get(session.getUser().getUsername(),argument.get(0));
        if(found == null ||found.isEmpty()){
            return new Response(
                    request.getVersion(),
                    Status.NOT_FOUND,
                    "Resource Not Found"
            );
        }
      return new Response(request.getVersion(),Status.OK,found);

    }
}
