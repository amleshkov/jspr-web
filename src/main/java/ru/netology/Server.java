package ru.netology;

import com.sun.source.tree.Scope;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int listenPort;
    private int threadsNumber;
    private ServerSocket serverSocket;
    private Socket socket;

    public Server(int listenPort, int threadsNumber) {
        this.listenPort = listenPort;
        this.threadsNumber = threadsNumber;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(listenPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
            executorService.submit(new ConnectionHandler(socket));
        }
    }
}
