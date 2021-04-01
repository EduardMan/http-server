package tech.itpark.http;

import com.google.gson.Gson;
import tech.itpark.http.annotation.RequestMapping;
import tech.itpark.http.annotation.header.RequestBody;
import tech.itpark.http.annotation.header.RequestHeader;
import tech.itpark.http.enums.HttpMethod;
import tech.itpark.http.enums.HttpStatus;
import tech.itpark.http.exception.MethodAlreadyRegistered;
import tech.itpark.http.model.User;
import tech.itpark.http.model.infrastructure.HandlerMethod;
import tech.itpark.http.model.infrastructure.HttpRequest;
import tech.itpark.http.model.infrastructure.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController {
    private final Map<String, Map<HttpMethod, HandlerMethod>> routes = new HashMap<>();
    private final Gson gson;

    public FrontController(Gson gson) {
        this.gson = gson;
    }

    void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws InvocationTargetException, IllegalAccessException {
        final Map<HttpMethod, HandlerMethod> route = routes.get(httpRequest.getRequestUrl());

        if (route == null) {
            httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            return;
        }

        final HandlerMethod handlerMethod = route.get(httpRequest.getMethod());

        List<Object> args = new ArrayList<>();
        final Parameter[] parameters = handlerMethod.getMethod().getParameters();
        for (Parameter parameter :
                parameters) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                args.add(handleRequestBody(parameter, httpRequest));
            }
            if (parameter.isAnnotationPresent(RequestHeader.class)) {
                args.add(handleRequestHeader(parameter, httpRequest));
            }
            if (parameter.getType() == HttpRequest.class) {
                args.add(httpRequest);
            }
        }

        final Object methodExecutionResult = handlerMethod.getMethod().invoke(handlerMethod.getBean(), args.toArray());

        if (methodExecutionResult instanceof String) {
            httpResponse.setBody(methodExecutionResult.toString().getBytes());
            return;
        }

        if (methodExecutionResult instanceof byte[]) {
            httpResponse.setBody((byte[]) methodExecutionResult);
            return;
        }

        httpResponse.setBody(gson.toJson(methodExecutionResult).getBytes());
    }

    private Object handleRequestHeader(Parameter parameter, HttpRequest httpRequest) {
        final RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);

        return httpRequest.getHeaders().get(annotation.value());
    }

    private Object handleRequestBody(Parameter parameter, HttpRequest httpRequest) {
        return gson.fromJson(new String(httpRequest.getBody()), parameter.getType());
    }

    public void registerRoute(Object bean, Method method, RequestMapping annotation) {

        if (routes.containsKey(annotation.path()) && routes.get(annotation.path()).containsKey(annotation.httpMethod())) {
            throw new MethodAlreadyRegistered(annotation.httpMethod() + " " + annotation.path() + " already registered");
        }

        final Map<HttpMethod, HandlerMethod> httpMethodHandlerMethodMap = routes.get(annotation.path());
        if (httpMethodHandlerMethodMap != null) {
            httpMethodHandlerMethodMap.put(annotation.httpMethod(), new HandlerMethod(bean, method));
            return;
        }

        routes.put(annotation.path(), new HashMap<>(Map.of(annotation.httpMethod(), new HandlerMethod(bean, method))));
    }
}
