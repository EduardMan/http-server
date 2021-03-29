package tech.itpark.http;

import tech.itpark.http.enums.HttpStatus;

public class Main {
    public static void main(String[] args) {

        final Server serverImproved = new Server();

        serverImproved.GET("/", (httpRequest, httpResponse) -> {
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.setBody("{\n   'id:' 22\n}".getBytes());
        });

        serverImproved.POST("/", (httpRequest, httpResponse) -> {
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.setBody(httpRequest.getBody());
        });

        serverImproved.listen(9999);

    }
}
