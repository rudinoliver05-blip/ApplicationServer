package com.example.jtcp.protocol;

import java.util.List;

public class Response {

    private final String version;
    private final Status status;
    private final String message;
    private final List<String> messages;

    public Response(String version, Status status, String message) {
        this.version = version;
        this.status = status;
        this.message = message;
        this.messages = null;
    }

    public Response(String version, Status status, List<String> messages) {
        this.version = version;
        this.status = status;
        this.message = null;
        this.messages = messages;
    }

    public String getVersion() {
        return version;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }
}