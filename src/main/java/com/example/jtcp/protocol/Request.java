package com.example.jtcp.protocol;

import java.util.List;

public class Request {

    private final String version;
    private final Command command;
    private final String sessionId;
    private final List<String> arguments;

    public Request(
            String version,
            Command command,
            String sessionId,
            List<String> arguments
    ) {
        this.version = version;
        this.command = command;
        this.sessionId = sessionId;
        this.arguments = arguments;
    }

    public String getVersion() {
        return version;
    }

    public Command getCommand() {
        return command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<String> getArguments() {
        return arguments;
    }
}