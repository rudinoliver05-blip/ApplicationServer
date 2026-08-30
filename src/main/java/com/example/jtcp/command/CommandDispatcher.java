package com.example.jtcp.command;

import com.example.jtcp.data.InMemoryDataStore;
import com.example.jtcp.handler.*;
import com.example.jtcp.protocol.*;
import com.example.jtcp.session.InMemorySessionStore;
import com.example.jtcp.userdetails.InMemoryUserStore;
import com.example.jtcp.userdetails.UserStore;

import java.util.Map;

public class CommandDispatcher {
    UserStore userStore = new InMemoryUserStore();
    InMemorySessionStore inMemorySessionStore=new InMemorySessionStore();
    InMemoryDataStore inMemoryDataStore=new InMemoryDataStore();
    private final Map<Command, CommandHandler> handlers = Map.ofEntries(
            Map.entry(Command.HELLO, new HelloHandler()),
            Map.entry(Command.SIGNUP, new SignupHandler(userStore)),
            Map.entry(Command.LOGIN, new LoginHandler(userStore,inMemorySessionStore)),
            Map.entry(Command.LOGOUT, new LogoutHandler(inMemorySessionStore)),
            Map.entry(Command.WHOAMI, new WhoAmIHandler(inMemorySessionStore)),
            Map.entry(Command.HELP, new HelpHandler()),
            Map.entry(Command.PING, new PingHandler()),
            Map.entry(Command.TIME, new TimeHandler()),
            Map.entry(Command.SET, new SetHandler(inMemorySessionStore,inMemoryDataStore)),
            Map.entry(Command.GET, new GetHandler(inMemorySessionStore,inMemoryDataStore)),
            Map.entry(Command.DELETE, new DeleteHandler(inMemorySessionStore,inMemoryDataStore)),
            Map.entry(Command.LIST, new ListHandler(inMemorySessionStore,inMemoryDataStore)),
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
