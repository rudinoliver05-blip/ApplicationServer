package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Command;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HelpHandler implements CommandHandler {

    @Override
    public Response handle(Request request) {

        String commands = Arrays.stream(Command.values())
                .map(Command::name)
                .collect(Collectors.joining(", "));

        return new Response(
                request.getVersion(),
                Status.OK,
                commands
        );
    }
}