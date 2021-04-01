package tech.itpark.http;

import tech.itpark.http.enums.HttpMethod;
import tech.itpark.http.enums.HttpStatus;
import tech.itpark.http.exception.HttpServerException;
import tech.itpark.http.exception.MethodAlreadyRegistered;
import tech.itpark.http.model.infrastructure.HttpRequest;
import tech.itpark.http.model.infrastructure.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Server {

    private final HashMap<String, BiConsumer<HttpRequest, HttpResponse>> route = new HashMap<>();

    public void listen(int port) {
        try (
                final var serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    handleConnection(socket);
                } catch (Exception e) {
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

            if (!httpRequest.getRequestUrl().startsWith(String.valueOf(route.keySet().toArray()[0]))) {
                httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
                out.write(httpResponse.getAsBytes());
                return;
            }

            final BiConsumer<HttpRequest, HttpResponse> httpRequestHandler = (BiConsumer<HttpRequest, HttpResponse>) route.values().toArray()[0];

            try {
                httpRequestHandler.accept(httpRequest, httpResponse);
            } catch (Exception e) {
                httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                out.write(httpResponse.getAsBytes());
                throw new HttpServerException(e);
            }

            out.write(httpResponse.getAsBytes());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String path, BiConsumer<HttpRequest, HttpResponse> registrator) {
        if (!route.isEmpty()) {
            throw new HttpServerException("Only one root route can be registered");
        }

        route.put(path, registrator);
    }

    /**
     * Map for using in level 0
     */
    @Deprecated
    private final HashMap<String, EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>>> routeForLevel0 = new HashMap<>();

    /**
     * This method was used for level 0
     */
    @Deprecated
    public void GET(String path, BiConsumer<HttpRequest, HttpResponse> registrator) {
        final EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>> httpMethodBiConsumerEnumMap = routeForLevel0.get(path);

        if (httpMethodBiConsumerEnumMap != null && httpMethodBiConsumerEnumMap.containsKey(HttpMethod.GET)) {
            throw new MethodAlreadyRegistered("GET " + path + " already registered");
        }

        if (httpMethodBiConsumerEnumMap != null) {
            routeForLevel0.get(path).put(HttpMethod.GET, registrator);
            return;
        }

        routeForLevel0.put(path, new EnumMap<>(Map.of(HttpMethod.GET, registrator)));
    }

    /**
     * This method was used for level 0
     */
    @Deprecated
    public void POST(String path, BiConsumer<HttpRequest, HttpResponse> registrator) {
        final EnumMap<HttpMethod, BiConsumer<HttpRequest, HttpResponse>> httpMethodBiConsumerEnumMap = routeForLevel0.get(path);

        if (httpMethodBiConsumerEnumMap != null && httpMethodBiConsumerEnumMap.containsKey(HttpMethod.POST)) {
            throw new MethodAlreadyRegistered("POST " + path + " already registered");
        }

        if (httpMethodBiConsumerEnumMap != null) {
            routeForLevel0.get(path).put(HttpMethod.POST, registrator);
            return;
        }

        routeForLevel0.put(path, new EnumMap<>(Map.of(HttpMethod.POST, registrator)));
    }

    /**
     * Method was used in level 0
     */
    @Deprecated
    private void handleConnectionLevel0(Socket socket) {
        try (
                socket;
                final var in = new BufferedInputStream(socket.getInputStream());
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            final HttpRequest httpRequest = new HttpRequest(in);
            final HttpResponse httpResponse = new HttpResponse();

            if (!routeForLevel0.containsKey(httpRequest.getRequestUrl())) {
                httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
                out.write(httpResponse.getAsBytes());
                return;
            }

            if (!routeForLevel0.get(httpRequest.getRequestUrl()).containsKey(httpRequest.getMethod())) {
                httpResponse.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
                out.write(httpResponse.getAsBytes());
                return;
            }

            final BiConsumer<HttpRequest, HttpResponse> httpRequestHandler = routeForLevel0.get(httpRequest.getRequestUrl()).get(httpRequest.getMethod());
            httpRequestHandler.accept(httpRequest, httpResponse);

            out.write(httpResponse.getAsBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
