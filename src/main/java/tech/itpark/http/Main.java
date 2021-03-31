package tech.itpark.http;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.itpark.http.enums.HttpStatus;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {

        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("tech");

        final Server server = context.getBean(Server.class);

        server.register("/", (httpRequest, httpResponse) -> {
            try {
                context.getBean(FrontController.class).handle(httpRequest, httpResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        server.listen(9999);
    }

    @Deprecated
    private void registerHandlerExamplesLevelO(Server server) {
        server.GET("/", (httpRequest, httpResponse) -> {
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.setBody("{\n   'id:' 22\n}".getBytes());
        });

        server.POST("/", (httpRequest, httpResponse) -> {
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.setBody(httpRequest.getBody());
        });
    }
}
