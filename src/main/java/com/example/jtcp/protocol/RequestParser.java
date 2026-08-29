package com.example.jtcp.protocol;

import java.util.Arrays;
import java.util.List;

public class RequestParser {

    public Request parseString(String rawRequest) throws ProtocolException {

        if (rawRequest == null || rawRequest.isBlank()) {
            throw new ProtocolException(ProtocolMessage.EMPTY_REQUEST);
        }

        String[] requestParts = rawRequest.trim().split("\\s+");

        if (requestParts.length < 2) {
            throw new ProtocolException(ProtocolMessage.MISSING_COMMAND);
        }

        ProtocolValidator validator = new ProtocolValidator();

        validator.checkVersion(requestParts[0]);

        Command command = validator.checkCommand(requestParts[1]);

        String sessionId = null;
        int argumentStart = 2;

        if (command != Command.LOGIN && command != Command.SIGNUP) {

            if (requestParts.length < 3) {
                throw new ProtocolException(ProtocolMessage.MISSING_SESSION);
            }

            sessionId = requestParts[2];
            argumentStart = 3;
        }

        List<String> arguments =
                Arrays.asList(requestParts)
                        .subList(argumentStart, requestParts.length);

        validator.checkArguments(command, arguments);

        return new Request(
                requestParts[0],
                command,
                sessionId,
                arguments
        );
    }
}