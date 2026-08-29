package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.session.Session;
import com.example.jtcp.session.SessionStore;
import com.example.jtcp.storage.User;
import com.example.jtcp.storage.UserStore;

import java.awt.*;
import java.util.UUID;

public class LoginHandler implements CommandHandler {

    private final UserStore userStore;
    private final SessionStore sessionStore;

    public LoginHandler(UserStore userStore, SessionStore sessionStore) {
        this.userStore = userStore;
        this.sessionStore = sessionStore;
    }

    @Override
    public Response handle(Request request) {

        String username = request.getArguments().get(0);
        String password = request.getArguments().get(1);
        System.out.println(username+" "+password);
        System.out.println("Login store: " + userStore);
        User user = userStore.find(username);
        System.out.println(user.getUsername()+" "+user.getPassword());
        if (user == null || !user.getPassword().equals(password)) {
            return new Response(
                    request.getVersion(),
                    Status.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

        String sessionId = UUID.randomUUID().toString();

        Session session = new Session(sessionId, user);
        sessionStore.save(session);

        return new Response(
                request.getVersion(),
                Status.OK,
                sessionId
        );
    }
}