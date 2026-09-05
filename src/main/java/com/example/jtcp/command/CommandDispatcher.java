package com.example.jtcp.command;

import com.example.jtcp.Auth.AuthenticationService;
import com.example.jtcp.data.InMemoryDataStore;
import com.example.jtcp.handler.*;
import com.example.jtcp.protocol.*;
import com.example.jtcp.session.InMemorySessionStore;
import com.example.jtcp.session.SessionService;
import com.example.jtcp.session.SessionStore;
import com.example.jtcp.userdetails.InMemoryUserStore;
import com.example.jtcp.userdetails.UserStore;

import java.time.Duration;
import java.util.Map;

public class CommandDispatcher {
    UserStore userStore = new InMemoryUserStore();
    SessionStore inMemorySessionStore=new InMemorySessionStore();
    InMemoryDataStore inMemoryDataStore=new InMemoryDataStore();
    AuthenticationService authenticationService =
            new AuthenticationService(
                    userStore,
                    inMemorySessionStore,
                    Duration.ofMinutes(30)
            );
    SessionService sessionService =
            new SessionService(inMemorySessionStore);
    private final Map<Command, CommandHandler> handlers = Map.ofEntries(
            Map.entry(Command.HELLO, new HelloHandler()),
            Map.entry(Command.SIGNUP, new SignupHandler(userStore)),
            Map.entry(Command.LOGIN, new LoginHandler(authenticationService)),
            Map.entry(Command.LOGOUT, new LogoutHandler(sessionService)),
            Map.entry(Command.WHOAMI, new WhoAmIHandler(sessionService)),
            Map.entry(Command.HELP, new HelpHandler()),
            Map.entry(Command.PING, new PingHandler()),
            Map.entry(Command.TIME, new TimeHandler()),
            Map.entry(Command.SET, new SetHandler(sessionService,inMemoryDataStore)),
            Map.entry(Command.GET, new GetHandler(sessionService,inMemoryDataStore)),
            Map.entry(Command.DELETE, new DeleteHandler(sessionService,inMemoryDataStore)),
            Map.entry(Command.LIST, new ListHandler(sessionService,inMemoryDataStore)),
            Map.entry(Command.QUIT, new QuitHandler())
    );
    public Response dispatch(Request request) throws ProtocolException {
        CommandHandler handler = handlers.get(request.getCommand());
        if (handler == null) {
            throw new ProtocolException(ProtocolMessage.INVALID_COMMAND);
        }
        return handler.handle(request);
    }
}
