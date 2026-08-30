package com.example.jtcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class Client {
    public static final int PORT=8010;
    private String sessionId;
    public String getSessionId() {
        return sessionId;
    }
    public void start() throws UnknownHostException, IOException {
        InetAddress address=InetAddress.getByName("localhost");
       try(Socket clientSocket=new Socket(address,PORT); PrintWriter toServer=new PrintWriter(clientSocket.getOutputStream(),true);
           BufferedReader fromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           Scanner scanner = new Scanner(System.in)){
           while (true) {
               System.out.print("> ");

               String input = scanner.nextLine();

               String message;

               if (input.startsWith("LOGIN") || input.startsWith("SIGNUP")) {
                   message = "JTCP/1.0 " + input;
               } else {
                   message = "JTCP/1.0 " + input + " " + sessionId;
               }

               toServer.println(message);

               String response = fromServer.readLine();

               if (input.startsWith("LOGIN")) {
                   sessionId = response;
               }
               System.out.println("Server: " + response);
              if(response.equals("Goodbye")){
                  break;
              }
           }
       }
    }
    public static void main(String[] args){
        try{
            Client client=new Client();
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
