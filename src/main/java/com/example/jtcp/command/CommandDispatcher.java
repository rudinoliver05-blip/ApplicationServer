package com.example.jtcp.command;

import com.example.jtcp.handler.*;
import com.example.jtcp.protocol.*;
import com.example.jtcp.session.InMemorySessionStore;
import com.example.jtcp.storage.InMemoryUserStore;
import com.example.jtcp.storage.UserStore;

import java.util.Map;

public class CommandDispatcher {
    UserStore userStore = new InMemoryUserStore();
    InMemorySessionStore inMemorySessionStore=new InMemorySessionStore();
    private final Map<Command, CommandHandler> handlers = Map.ofEntries(
            Map.entry(Command.HELLO, new HelloHandler()),
            Map.entry(Command.SIGNUP, new SignupHandler(userStore)),
            Map.entry(Command.LOGIN, new LoginHandler(userStore,inMemorySessionStore)),
            Map.entry(Command.LOGOUT, new LogoutHandler(inMemorySessionStore)),
            Map.entry(Command.WHOAMI, new WhoAmIHandler()),
            Map.entry(Command.HELP, new HelpHandler()),
            Map.entry(Command.PING, new PingHandler()),
            Map.entry(Command.TIME, new TimeHandler()),
            Map.entry(Command.SET, new SetHandler()),
            Map.entry(Command.GET, new GetHandler()),
            Map.entry(Command.DELETE, new DeleteHandler()),
            Map.entry(Command.LIST, new ListHandler()),
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
