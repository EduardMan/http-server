package tech.itpark.http;

import tech.itpark.http.exception.MethodAlreadyRegistered;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ServerImproved {
    private final HashMap<String, EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>>> route = new HashMap<>();

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

            if (!route.containsKey(httpRequest.getRequestUrl())) {
                httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
                out.write(httpResponse.getAsBytes());
                return;
            }

            if (!route.get(httpRequest.getRequestUrl()).containsKey(httpRequest.getMethod())) {
                httpResponse.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
                out.write(httpResponse.getAsBytes());
                return;
            }

            final BiConsumer<HttpRequest, HttpResponse> httpRequestHandler = route.get(httpRequest.getRequestUrl()).get(httpRequest.getMethod());
            httpRequestHandler.accept(httpRequest, httpResponse);

            out.write(httpResponse.getAsBytes());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GET(String path, BiConsumer<HttpRequest, HttpResponse> registrator) {
        final EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>> httpMethodBiConsumerEnumMap = route.get(path);

        if (httpMethodBiConsumerEnumMap != null && httpMethodBiConsumerEnumMap.containsKey(HttpMethod.GET)) {
            throw new MethodAlreadyRegistered("GET " + path + " already registered");
        }

        if (httpMethodBiConsumerEnumMap != null) {
            route.get(path).put(HttpMethod.GET, registrator);
            return;
        }

        route.put(path, new EnumMap<>(Map.of(HttpMethod.GET, registrator)));
    }

    public void POST(String path, BiConsumer<HttpRequest, HttpResponse> registrator) {
        final EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>> httpMethodBiConsumerEnumMap = route.get(path);

        if (httpMethodBiConsumerEnumMap != null && httpMethodBiConsumerEnumMap.containsKey(HttpMethod.POST)) {
            throw new MethodAlreadyRegistered("POST " + path + " already registered");
        }

        if (httpMethodBiConsumerEnumMap != null) {
            route.get(path).put(HttpMethod.POST, registrator);
            return;
        }

        route.put(path, new EnumMap<>(Map.of(HttpMethod.POST, registrator)));
    }
}
