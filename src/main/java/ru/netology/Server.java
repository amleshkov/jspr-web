package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    ExecutorService executorService;

    public Server(int threadsNumber) {
        this.executorService = Executors.newFixedThreadPool(threadsNumber);
    }

    public void start(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                try {
                    executorService.submit(new ConnectionHandler(serverSocket.accept()));
                } catch (IOException e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
