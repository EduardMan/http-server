package tech.itpark.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerImproved {
    private final HashMap<String, Registrator> route = new HashMap<>();

    public void listen(int port) {
        try (
                final var serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    handleConnection(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try (
                socket;
                final var in = new BufferedInputStream(socket.getInputStream());
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            final HttpRequest httpRequest = new HttpRequest(in);
            final HttpResponse httpResponse = new HttpResponse();

            route.get(httpRequest.getRequestUrl()).call(httpRequest, httpResponse);

            out.write(httpResponse.getAsBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String path, Registrator registrator) {
        route.put(path, registrator);
    }
}
