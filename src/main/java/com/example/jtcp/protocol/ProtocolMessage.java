package com.example.jtcp.protocol;

public final class ProtocolMessage {

    public static final String EMPTY_REQUEST =
            "Request cannot be empty";
    public static final String ARGUMENT_COUNT_MISMATCH =
            "Invalid argument count: %s requires %d argument(s), but %d were provided";

    public static final String MISSING_COMMAND =
            "Request must contain version and command";

    public static final String INVALID_VERSION =
            "Unsupported protocol version";

    public static final String INVALID_COMMAND =
            "Unknown command";
    public static final String MISSING_SESSION =
            "No session was found";


    private ProtocolMessage() {
    }
}