package com.example.jtcp.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseParserTest {

    private final ResponseParser parser = new ResponseParser();

    @Test
    void parsesValidResponse() throws ProtocolException {

        Response response =
                parser.parse("JTCP/1.0 200 OK Login successful");

        assertEquals("JTCP/1.0", response.getVersion());
        assertEquals(Status.OK, response.getStatus());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void rejectsEmptyResponse() {

        assertThrows(
                ProtocolException.class,
                () -> parser.parse("")
        );
    }

    @Test
    void rejectsMalformedResponse() {

        assertThrows(
                ProtocolException.class,
                () -> parser.parse("JTCP/1.0 200")
        );
    }

    @Test
    void rejectsUnsupportedVersion() {

        assertThrows(
                ProtocolException.class,
                () -> parser.parse("JTCP/2.0 200 OK Success")
        );
    }

    @Test
    void rejectsUnknownStatusCode() {

        assertThrows(
                ProtocolException.class,
                () -> parser.parse("JTCP/1.0 999 UNKNOWN Something")
        );
    }
    @Test
    void rejectsInconsistentStatusName() {

        assertThrows(
                ProtocolException.class,
                () -> parser.parse(
                        "JTCP/1.0 200 UNAUTHORIZED Success"
                )
        );
    }

    @Test
    void preservesMessageContainingSpaces()
            throws ProtocolException {

        Response response =
                parser.parse(
                        "JTCP/1.0 400 BAD_REQUEST Invalid username or password"
                );

        assertEquals(
                "Invalid username or password",
                response.getMessage()
        );
    }
}