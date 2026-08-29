package com.example.jtcp;

import com.example.jtcp.command.CommandDispatcher;
import com.example.jtcp.protocol.ProtocolException;
import com.example.jtcp.protocol.Request;
import com.example.jtcp.protocol.RequestParser;
import com.example.jtcp.protocol.Response;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 8010;
    public void start() throws IOException {
        System.out.println("JTCP Server starting...");
        try (
                ServerSocket socket = new ServerSocket(PORT);
                Socket acceptedConnection = socket.accept();
                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()))
        ) {

            System.out.println("Client accepted\nThe client address id " + acceptedConnection.getInetAddress());
            RequestParser parser = new RequestParser();
            CommandDispatcher dispatcher = new CommandDispatcher();
            while (true) {
                String clientMessage = fromClient.readLine();
                if (clientMessage == null) {
                    break;
                }
                try{
                   Request request= parser.parseString(clientMessage);
                   Response response=dispatcher.dispatch(request);
                   toClient.println(response.getMessage());
                }
                catch (ProtocolException ex){
                    toClient.println(ex.getMessage());
                }
                System.out.println("Client: " + clientMessage);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}