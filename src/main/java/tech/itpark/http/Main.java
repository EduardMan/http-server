package tech.itpark.http;

public class Main {
    public static void main(String[] args) {

        final ServerImproved serverImproved = new ServerImproved();

        serverImproved.register("/", (httpRequest, httpResponse) -> {
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.setBody("{\n   'id:' 22\n}".getBytes());
        });

        serverImproved.listen(9999);

    }
}
