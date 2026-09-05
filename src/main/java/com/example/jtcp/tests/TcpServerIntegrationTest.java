package com.example.jtcp.tests;

import com.example.jtcp.Server;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TcpServerIntegrationTest {

    private static Server server;
    private static Thread serverThread;

    @BeforeAll
    static void startServer() throws InterruptedException {

        server = new Server();

        serverThread = new Thread(() -> {

            try {
                server.start();

            } catch (IOException e) {

                if (serverThread.isInterrupted()) {
                    return;
                }

                e.printStackTrace();
            }

        }, "JTCP-Test-Server");

        serverThread.start();

        // Temporary startup synchronization.
        Thread.sleep(500);
    }

    @AfterAll
    static void stopServer() throws InterruptedException {

        server.stop();

        serverThread.join(2000);
    }

    @Test
    void serverAcceptsTcpConnection() throws IOException {

        try (
                Socket socket =
                        new Socket("localhost", Server.PORT);

                BufferedReader fromServer =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        )
        ) {

            String response = fromServer.readLine();

            assertNotNull(response);

            assertEquals(
                    "JTCP/1.0 READY",
                    response
            );
        }
    }
}