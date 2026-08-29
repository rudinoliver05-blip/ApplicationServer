package com.example.jtcp.command;

import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.Response;

public interface CommandHandler {
    public Response handle(Request request);
}
