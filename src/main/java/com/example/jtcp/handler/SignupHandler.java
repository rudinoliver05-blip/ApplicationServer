package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;
import com.example.jtcp.storage.User;
import com.example.jtcp.storage.UserStore;

public class SignupHandler implements CommandHandler {

    private final UserStore userStore;

    public SignupHandler(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public Response handle(Request request) {

        String username = request.getArguments().get(0);
        String password = request.getArguments().get(1);

        if (userStore.exists(username)) {
            return new Response(
                    request.getVersion(),
                    Status.CONFLICT,
                    "User already exists"
            );
        }

        User user = new User(username, password);
        System.out.println("Signup store: " + userStore);
        userStore.save(user);

        return new Response(
                request.getVersion(),
                Status.CREATED,
                "User created successfully"
        );
    }
}