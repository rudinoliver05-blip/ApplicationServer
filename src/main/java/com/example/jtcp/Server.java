package com.example.jtcp;

import com.example.jtcp.command.CommandDispatcher;
import com.example.jtcp.protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static final int PORT = 8010;

    private final AtomicInteger workerNumber = new AtomicInteger(1);

    private volatile boolean running = true;

    private ServerSocket serverSocket;

    private final Set<Socket> activeConnections =
            new ConcurrentHashMap<>().newKeySet();

    private final ThreadFactory threadFactory = task -> {
        Thread thread = new Thread(
                task,
                "JTCP-Worker-" + workerNumber.getAndIncrement()
        );
        return thread;
    };

    private final ExecutorService executor =
            new ThreadPoolExecutor(
                    2,
                    2,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1),
                    threadFactory,
                    new ThreadPoolExecutor.AbortPolicy()
            );

    public void registerShutDown() {

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        this::stop,
                        "JTCP-Shutdown"
                )
        );
    }

    public void stop() {

        if (!running) {
            return;
        }

        System.out.println("System is shutting down");

        running = false;

        if (serverSocket != null && !serverSocket.isClosed()) {

            try {

                serverSocket.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        closeActiveConnections();

        executor.shutdown();

        try {

            // Give existing workers time to finish
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {

                System.out.println(
                        "The executor did not finish in time"
                );

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();

            Thread.currentThread().interrupt();
        }

        System.out.println("JTCP shutdown complete");
    }

    private void closeActiveConnections() {

        System.out.println(
                "Closing " + activeConnections.size()
                        + " active connections"
        );

        for (Socket connection : activeConnections) {

            try {

                System.out.println(
                        "Closing connection: " + connection
                );

                connection.close();

            } catch (IOException e) {

                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleClient(
            Socket acceptedConnection,
            RequestParser parser,
            CommandDispatcher dispatcher
    ) throws IOException {

        activeConnections.add(acceptedConnection);

        try (
                Socket connection = acceptedConnection;

                PrintWriter toClient =
                        new PrintWriter(
                                connection.getOutputStream(),
                                true
                        );

                BufferedReader fromClient =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()
                                )
                        )
        ) {

            System.out.println(
                    "Client accepted\nThe client address id "
                            + acceptedConnection.getInetAddress()
            );

            toClient.println("JTCP/1.0 READY");

            while (true) {

                String clientMessage =
                        fromClient.readLine();

                if (clientMessage == null) {
                    break;
                }

                try {

                    Request request =
                            parser.parseString(clientMessage);

                    Response response =
                            dispatcher.dispatch(request);

                    ResponseSerializer serializer =
                            new ResponseSerializer();

                    String serializedResponse =
                            serializer.serialize(response);

                    System.out.println(
                            "Sending response: ["
                                    + serializedResponse
                                    + "]"
                    );

                    toClient.println(serializedResponse);

                } catch (ProtocolException ex) {

                    Response errorResponse =
                            new Response(
                                    Protocol.getVersion(),
                                    Status.BAD_REQUEST,
                                    ex.getMessage()
                            );

                    ResponseSerializer serializer =
                            new ResponseSerializer();

                    String serializedResponse =
                            serializer.serialize(errorResponse);

                    System.out.println(
                            "Sending protocol error: ["
                                    + serializedResponse
                                    + "]"
                    );

                    toClient.println(serializedResponse);

                } catch (RuntimeException ex) {

                    System.err.println(
                            "Unexpected error while processing request: "
                                    + ex.getMessage()
                    );

                    Response errorResponse =
                            new Response(
                                    Protocol.getVersion(),
                                    Status.INTERNAL_ERROR,
                                    "Internal server error"
                            );

                    ResponseSerializer serializer =
                            new ResponseSerializer();

                    String serializedResponse =
                            serializer.serialize(errorResponse);

                    toClient.println(serializedResponse);
                }

                System.out.println(
                        Thread.currentThread().getName()
                                + "=> Client: "
                                + clientMessage
                );
            }

        } finally {

            activeConnections.remove(acceptedConnection);
        }
    }

    public void start() throws IOException {

        System.out.println("JTCP Server starting...");

        try (ServerSocket socket =
                     new ServerSocket(PORT)) {

            this.serverSocket = socket;

            // Register JVM shutdown handling
            registerShutDown();

            RequestParser parser =
                    new RequestParser();

            CommandDispatcher dispatcher =
                    new CommandDispatcher();

            while (running) {

                try {

                    Socket acceptedConnection =
                            serverSocket.accept();

                    try {

                        executor.submit(() -> {

                            try {

                                handleClient(
                                        acceptedConnection,
                                        parser,
                                        dispatcher
                                );

                            } catch (IOException e) {

                                if (running) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (RejectedExecutionException e) {

                        System.out.println(
                                "REJECTED CLIENT"
                        );

                        try {

                            PrintWriter toClient =
                                    new PrintWriter(
                                            acceptedConnection.getOutputStream(),
                                            true
                                    );

                            toClient.println(
                                    "Server is busy. Try again later"
                            );

                            acceptedConnection.shutdownOutput();

                            acceptedConnection.close();

                        } catch (IOException ex) {

                            if (running) {
                                throw e;
                            }

                            System.out.println(
                                    "Server accept loop stopped."
                            );
                        }
                    }

                } catch (IOException e) {

                    if (running) {
                        throw e;
                    }

                    System.out.println(
                            "Server accept loop stopped."
                    );
                }
            }
        }
    }

    public static void main(String[] args) {

        Server server = new Server();

        try {

            server.start();

        } catch (Exception e) {

            if (server.running) {
                e.printStackTrace();
            }
        }
    }
}