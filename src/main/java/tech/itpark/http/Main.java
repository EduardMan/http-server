package tech.itpark.http;

public class Main {
    public static void main(String[] args) {

        final ServerImproved serverImproved = new ServerImproved();

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
