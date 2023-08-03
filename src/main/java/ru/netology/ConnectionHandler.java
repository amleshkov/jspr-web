package ru.netology;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class ConnectionHandler implements Runnable {
    private static String CONTENT = "public";

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
              final var in = new BufferedReader((new InputStreamReader((socket.getInputStream()))));
              final var out = new BufferedOutputStream(socket.getOutputStream());
              ) {
            final var request = in.readLine();
            final var requestParts = request.split("\s");
            // log
            if (requestParts.length != 3) {
                out.write(HTTPResponse.badRequestResponse());
                out.flush();
                socket.close();
                return;
            }
            final var method = requestParts[0];
            if (!method.equals(Methods.GET.toString())) {
                out.write(HTTPResponse.badRequestResponse());
                out.flush();
                socket.close();
                return;
            }
            final var resource = requestParts[1];
            if (!Resources.validPaths.contains(resource)) {
                out.write(HTTPResponse.notFoundResponse());
                out.flush();
                socket.close();
                return;
            }
            final var path = Path.of(".", CONTENT, resource);
            final var mimeType = Files.probeContentType(path);
            if (resource.equals("/classic.html")) {
                final var template = Files.readString(path);
                final var content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                out.write(HTTPResponse.okResponse(mimeType, Long.valueOf(content.length)));
                out.write(content);
                out.flush();
                socket.close();
                return;
            }
            final var length = Files.size(path);
            out.write(HTTPResponse.okResponse(mimeType, length));
            Files.copy(path, out);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
