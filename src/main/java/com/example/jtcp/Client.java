package com.example.jtcp;

import com.example.jtcp.protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static final int PORT = 8010;

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void start() throws UnknownHostException, IOException {

        InetAddress address = InetAddress.getByName("localhost");

        try (
                Socket clientSocket = new Socket(address, PORT);
                PrintWriter toServer =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader fromServer =
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {

            ResponseParser responseParser = new ResponseParser();

            String handshake = fromServer.readLine();

            if (handshake == null) {
                return;
            }

            System.out.println("Server: " + handshake);

            if (handshake.contains("BUSY")) {
                return;
            }

            while (true) {

                System.out.print("> ");

                String input = scanner.nextLine();

                String message;

                String commandName = input.trim().split("\\s+")[0];

                try {

                    Command command = Command.valueOf(commandName);

                    if (command.isAuthenticationRequired()) {
                        message = "JTCP/1.0 " + input + " " + sessionId;
                    } else {
                        message = "JTCP/1.0 " + input;
                    }

                } catch (IllegalArgumentException e) {

                    message = "JTCP/1.0 " + input;
                }

                toServer.println(message);

                String rawResponse = fromServer.readLine();

                if (rawResponse == null) {
                    break;
                }

                try {

                    Response response = responseParser.parse(rawResponse);

                    if (input.startsWith("LOGIN")
                            && response.getStatus() == Status.OK) {

                        sessionId = response.getMessage();
                    }

                    if (input.startsWith("LOGOUT")
                            && response.getStatus() == Status.OK) {

                        sessionId = null;
                    }

                    System.out.println("Server: " + response.getMessage());

                    if (input.startsWith("QUIT")) {
                        break;
                    }

                } catch (ProtocolException e) {

                    System.out.println("Server: " + rawResponse);
                }
            }
        }
    }

    public static void main(String[] args) {

        try {
            Client client = new Client();
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}