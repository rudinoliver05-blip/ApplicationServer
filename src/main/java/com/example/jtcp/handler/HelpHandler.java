package com.example.jtcp.handler;

import com.example.jtcp.command.CommandHandler;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;

public class HelpHandler implements CommandHandler {
    @Override
    public Response handle(Request request) {
        return null;
    }
}
