package com.example.jtcp.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseSerializerTest {

    private final ResponseSerializer serializer =
            new ResponseSerializer();
    @Test
    void serializesErrorResponse() {

        Response response =
                new Response(
                        Protocol.getVersion(),
                        Status.UNAUTHORIZED,
                        "Invalid username or password"
                );

        String result = serializer.serialize(response);

        assertEquals(
                "JTCP/1.0 401 UNAUTHORIZED Invalid username or password",
                result
        );
    }
    @Test
    void serializedResponseCanBeParsedBack() throws ProtocolException {

        Response original =
                new Response(
                        Protocol.getVersion(),
                        Status.CONFLICT,
                        "Username already exists"
                );

        String serialized = serializer.serialize(original);

        ResponseParser parser = new ResponseParser();

        Response parsed = parser.parse(serialized);

        assertEquals(original.getVersion(), parsed.getVersion());
        assertEquals(original.getStatus(), parsed.getStatus());
        assertEquals(original.getMessage(), parsed.getMessage());
    }
    @Test
    void serializesSuccessfulResponse() {

        Response response =
                new Response(
                        Protocol.getVersion(),
                        Status.OK,
                        "Login successful"
                );

        String result = serializer.serialize(response);

        assertEquals(
                "JTCP/1.0 200 OK Login successful",
                result
        );
    }
}