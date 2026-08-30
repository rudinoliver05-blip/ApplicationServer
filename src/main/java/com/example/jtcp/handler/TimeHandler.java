package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;
import com.example.jtcp.protocol.Status;

import java.time.Instant;

public class TimeHandler implements CommandHandler {

    @Override
    public Response handle(Request request) {

        return new Response(
                request.getVersion(),
                Status.OK,
                Instant.now().toString()
        );
    }
}