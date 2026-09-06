package com.example.jtcp.protocol;

public class ResponseSerializer {

    public String serialize(Response response) {

        return String.format(
                "%s %d %s %s",
                response.getVersion(),
                response.getStatus().getCode(),
                response.getStatus().name(),
                response.getMessage()
        );
    }
}