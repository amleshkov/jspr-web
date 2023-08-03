package ru.netology;

public class Main {
    static final int LISTEN_PORT = 9999;
    static final int THREADS_NUMBER = 64;

    public static void main(String[] args) {
        Server server = new Server(LISTEN_PORT, THREADS_NUMBER);
        server.start();
    }
}

