package com.example.jtcp.protocol;
public class ResponseParser {

    public Response parse(String rawResponse) throws ProtocolException {

        if (rawResponse == null || rawResponse.isBlank()) {
            throw new ProtocolException(ProtocolMessage.EMPTY_RESPONSE);
        }

        String[] parts = rawResponse.split("\\s+", 4);

        if (parts.length < 4) {
            throw new ProtocolException(ProtocolMessage.INVALID_RESPONSE);
        }

        String version = parts[0];
        if (!version.equals(Protocol.getVersion())) {
            throw new ProtocolException(ProtocolMessage.INVALID_VERSION);
        }
        int statusCode;
        try {
            statusCode = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new ProtocolException(ProtocolMessage.INVALID_RESPONSE);
        }

        Status status = null;

        for (Status value : Status.values()) {
            if (value.getCode() == statusCode) {
                status = value;
                break;
            }
        }

        if (status == null) {
            throw new ProtocolException(ProtocolMessage.INVALID_RESPONSE);
        }

        if (!status.name().equals(parts[2])) {
            throw new ProtocolException(ProtocolMessage.INVALID_RESPONSE);
        }

        String message = parts[3];

        return new Response(version, status, message);
    }
}