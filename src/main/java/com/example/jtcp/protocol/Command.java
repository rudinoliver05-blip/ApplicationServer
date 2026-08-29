package com.example.jtcp.protocol;

public enum Command {

    HELLO(0),
    SIGNUP(2),
    LOGIN(2),
    LOGOUT(0),
    WHOAMI(0),
    HELP(0),
    PING(0),
    TIME(0),
    SET(2),
    GET(1),
    DELETE(1),
    LIST(0),
    QUIT(0);

    private final int argumentCount;

    Command(int argumentCount) {
        this.argumentCount = argumentCount;
    }

    public int getArgumentCount() {
        return argumentCount;
    }
}