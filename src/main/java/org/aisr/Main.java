package org.aisr;

import org.aisr.service.AppInitializeService;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private final ServerSocket serverSocket;
    private static final int SERVER_PORT = 8888;

    public Main(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        try {
            DbConnectionManager dbManager = DbConnectionManager.getInstance();
            dbManager.initializeDatabase();
            System.out.println("The Server is listening on port: "+SERVER_PORT);
            AppInitializeService.createAdmin();
            while (true) {
                new ClientHandler(serverSocket.accept(), DbConnectionManager.getInstance().getConnection()).start();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Failed to start server: "+e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Server starting........");
        try {
            new Main(SERVER_PORT).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to start server: "+e.getMessage());
        }
    }
}