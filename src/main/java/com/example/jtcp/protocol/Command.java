package com.example.jtcp.protocol;

public enum Command {

    HELLO(0, false),
    SIGNUP(2, false),
    LOGIN(2, false),
    LOGOUT(0, true),
    WHOAMI(0, true),
    HELP(0, false),
    PING(0, false),
    TIME(0, false),
    SET(2, true),
    GET(1, true),
    DELETE(1, true),
    LIST(0, true),
    QUIT(0, false);

    private final int argumentCount;
    private final boolean authenticationRequired;

    Command(int argumentCount, boolean authenticationRequired) {
        this.argumentCount = argumentCount;
        this.authenticationRequired = authenticationRequired;
    }

    public int getArgumentCount() {
        return argumentCount;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }
}